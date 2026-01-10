package com.dotoryteam.dotory.domain.lifestyle.entity;

import com.dotoryteam.dotory.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_lifestyle")
public class MemberLifestyle {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lifestyle_id" , nullable = false)
    private Lifestyle lifestyle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id" , nullable = false , unique = true)
    private Member member;

    @Builder
    public MemberLifestyle(Lifestyle lifestyle, Member member) {
        this.lifestyle = lifestyle;
        this.member = member;
    }
}
