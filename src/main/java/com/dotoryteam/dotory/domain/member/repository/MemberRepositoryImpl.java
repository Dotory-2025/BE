package com.dotoryteam.dotory.domain.member.repository;

import com.dotoryteam.dotory.domain.lifestyle.entity.Lifestyle;
import com.dotoryteam.dotory.domain.member.dto.response.MemberSearchRes;
import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.domain.member.enums.UserStatus;
import com.dotoryteam.dotory.global.common.dto.CursorResult;
import com.dotoryteam.dotory.global.common.utils.CursorUtils;
import com.dotoryteam.dotory.global.image.utils.S3UrlGenerator;
import com.dotoryteam.dotory.global.security.enums.UserRole;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.dotoryteam.dotory.domain.alarm.entity.QNotification.notification;
import static com.dotoryteam.dotory.domain.auth.entity.QEmailVerification.emailVerification;
import static com.dotoryteam.dotory.domain.lifestyle.entity.QMemberLifestyle.memberLifestyle;
import static com.dotoryteam.dotory.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final S3UrlGenerator urlGenerator;

    public Optional<Member> findById(long id) {
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .where(member.id.eq(id))
                .fetchOne()
        );
    }

    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(queryFactory.select(member)
                .from(member)
                .innerJoin(member.emailVerification, emailVerification).fetchJoin()
                .where(emailVerification.email.eq(email))
                .fetchOne());
    }

    public Optional<Member> findByMemberUuid(UUID memberUuid) {
        return Optional.ofNullable(queryFactory.select(member)
                .from(member)
                .where(member.memberKey.eq(memberUuid))
                .fetchOne()
        );
    }

    public CursorResult<MemberSearchRes> searchMembers(
            UUID myMemberKey ,
            List<String> keywords ,
            Long minEntrance ,
            Long maxEntrance ,
            List<Lifestyle> lifestyles ,
            String cursorId ,
            int size
    ) {
        Long cursor = CursorUtils.parseCursor(cursorId);
        //select 에서 QMemberSearchRes 로 직접 사용하고 싶었는데 MemberLifestyle 을 강제 조인해버려서 검색하지도 않았는데 검색되어서 나옴
        //그냥 멤버로 받고 나중에 DTO 로 형 변환 해버리면 됨
        List<Member> members = queryFactory
                .selectFrom(member)
                .distinct() //  중복되어 검색되는거 방지
                .leftJoin(member.memberLifestyle, memberLifestyle) // 연관된 데이터 한번에 가져오기
                .where(
                        member.memberKey.ne(myMemberKey) ,
                        isSearchableMember() ,     //  검색 가능한 상태
                        EntranceYearEq(minEntrance, maxEntrance),
                        LifestyleIn(lifestyles),
                        SearchNicknamesIn(keywords),
                        ltCursorId(cursor)
                )
                .orderBy(member.id.asc())
                .limit(size + 1)
                .fetch();

        // DTO 변환
        List<MemberSearchRes> content = members.stream()
                .map(m -> new MemberSearchRes(
                        CursorUtils.toCursor(m.getId()) ,     //  cursor 때문에 반 필수
                        m.getMemberKey() ,
                        m.getNickname() ,
                        m.getMemberLifestyle() , // 이미 fetchJoin 으로 가져온 값이라서 다시 inner join 을 하지 않음
                        urlGenerator.createCloudFrontUrl(m.getProfileImgUrl())  //  cloudfront 도메인 붙임
                ))
                .collect(Collectors.toList());

        boolean hasNext = false;
        String nextCursorId = null;

        if (content.size() > size) {
            content.remove(size);
            hasNext = true;

            nextCursorId = content.get(content.size() - 1).getHashedId();
        }

        return new CursorResult<>(content , hasNext , nextCursorId);
    }

    private BooleanExpression EntranceYearEq(Long minEntrance, Long maxEntrance) {
        if (minEntrance != null && maxEntrance != null) {
            return member.entranceYear.between(minEntrance, maxEntrance);
        } else return null;
    }

    private BooleanExpression LifestyleIn(List<Lifestyle> lifestyles) {
        if (lifestyles != null && !lifestyles.isEmpty()) {
            return memberLifestyle.lifestyle.in(lifestyles);
        }
        return null;
    }

    private BooleanBuilder SearchNicknamesIn(List<String> nicknames) {
        if (nicknames == null || nicknames.isEmpty()) {
            return null;
        }

        BooleanBuilder builder = new BooleanBuilder();

        for (String nickname : nicknames) {
            builder.or(member.nickname.containsIgnoreCase(nickname));
        }

        return builder;
    }

    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return notification.id.lt(cursorId);
    }

    private BooleanBuilder isSearchableMember() {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(member.userStatus.eq(UserStatus.USER))
                .and(member.userRole.eq(UserRole.USER));

        return builder;
    }

    public Boolean nicknameDuplicate(String nickname) {
        Integer cnt = queryFactory
                .selectOne()
                .from(member)
                .where(member.nickname.eq(nickname))
                .fetchFirst();
        return cnt != null;
    }
}
