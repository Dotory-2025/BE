package com.dotoryteam.dotory.domain.member.repository;

import com.dotoryteam.dotory.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> , MemberRepositoryCustom {
}
