package com.dotoryteam.dotory.domain.member.repository;

import com.dotoryteam.dotory.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findByEmail(String email);
}
