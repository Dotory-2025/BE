package com.dotoryteam.dotory.domain.auth.repository;

import com.dotoryteam.dotory.domain.auth.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Long> {
}
