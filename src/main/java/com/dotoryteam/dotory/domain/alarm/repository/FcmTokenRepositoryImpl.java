package com.dotoryteam.dotory.domain.alarm.repository;

import com.dotoryteam.dotory.domain.alarm.entity.FcmToken;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dotoryteam.dotory.domain.alarm.entity.QFcmToken.fcmToken1;

@RequiredArgsConstructor
public class FcmTokenRepositoryImpl implements FcmTokenRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public boolean existsByFcmToken(String fcmToken) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(fcmToken1)
                .where(fcmToken1.fcmToken.eq(fcmToken))
                .fetchFirst();

        return fetchOne != null;
    }

    public List<FcmToken> findAllByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(fcmToken1)
                .where(fcmToken1.member.id.eq(memberId))
                .fetch();
    }
}
