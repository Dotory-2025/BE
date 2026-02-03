package com.dotoryteam.dotory.domain.lifestyle.repository;

import com.dotoryteam.dotory.domain.lifestyle.entity.Lifestyle;
import com.dotoryteam.dotory.domain.lifestyle.entity.MemberLifestyle;
import com.dotoryteam.dotory.domain.lifestyle.entity.QLifestyle;
import com.dotoryteam.dotory.domain.lifestyle.entity.QMemberLifestyle;
import com.dotoryteam.dotory.domain.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.dotoryteam.dotory.domain.lifestyle.entity.QLifestyle.lifestyle;
import static com.dotoryteam.dotory.domain.lifestyle.entity.QMemberLifestyle.memberLifestyle;
import static com.dotoryteam.dotory.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class MemberLifestyleRepositoryImpl implements MemberLifestyleRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public List<String> findKeywordsByMemberKey(UUID memberKey) {
        return queryFactory.select(lifestyle.name)
                .from(memberLifestyle)
                .join(memberLifestyle.member , member)
                .join(memberLifestyle.lifestyle , lifestyle)
                .where(member.memberKey.eq(memberKey))
                .fetch();
    }

    public void bulkDeleteByDeletedLifestyles(Long id) {
        queryFactory.delete(memberLifestyle)
                .where(memberLifestyle.lifestyle.id.eq(id))
                .execute();
    }
}
