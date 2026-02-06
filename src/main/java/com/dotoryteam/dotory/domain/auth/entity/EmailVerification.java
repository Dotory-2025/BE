package com.dotoryteam.dotory.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "email_verification")
public class EmailVerification {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(name = "is_verified" , nullable = false)
    private boolean isVerified;

    @Column(name = "first_verification_time" , nullable = false)
    private LocalDateTime firstVerificationTime;

    @Builder
    public EmailVerification(String email) {
        this.email = email;
        this.isVerified = true;
        this.firstVerificationTime = LocalDateTime.now();
    }
}
