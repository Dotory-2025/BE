package com.dotoryteam.dotory.domain.auth.repository;

import com.dotoryteam.dotory.domain.auth.entity.MemberTerms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTermsRepository extends JpaRepository<MemberTerms, Long> {
}
