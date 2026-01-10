package com.dotoryteam.dotory.domain.auth.entity;

import com.dotoryteam.dotory.domain.member.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MemberTerms {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id" , nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id" , nullable = false)
    private Terms terms;

    @Column(name = "term_agreement_at" , nullable = false)
    private LocalDateTime termAgreementAt;
}
