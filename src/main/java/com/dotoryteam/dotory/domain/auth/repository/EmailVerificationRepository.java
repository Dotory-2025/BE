package com.dotoryteam.dotory.domain.auth.repository;

import com.dotoryteam.dotory.domain.auth.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
}
