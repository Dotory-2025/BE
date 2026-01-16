package com.dotoryteam.dotory.domain.member.repository;

import com.dotoryteam.dotory.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.dotoryteam.dotory.domain.auth.entity.QEmailVerification.emailVerification;
import static com.dotoryteam.dotory.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(queryFactory.select(member)
                .from(member)
                .innerJoin(member.emailVerification, emailVerification).fetchJoin()
                .where(emailVerification.email.eq(email))
                .fetchOne());
    }
}
