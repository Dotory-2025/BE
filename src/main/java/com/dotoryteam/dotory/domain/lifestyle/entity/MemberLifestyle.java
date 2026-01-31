package com.dotoryteam.dotory.domain.lifestyle.entity;

import com.dotoryteam.dotory.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_lifestyle", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_member_lifestyle",
                columnNames = {"member_id", "lifestyle_id"}
        )
})
public class MemberLifestyle {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lifestyle_id" , nullable = false)
    private Lifestyle lifestyle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id" , nullable = false)
    private Member member;

    @Builder
    public MemberLifestyle(Member member , Lifestyle lifestyle) {
        this.member = member;
        this.lifestyle = lifestyle;
    }

    // 정적 팩토리 메서드
    public static MemberLifestyle of(Member member, Lifestyle lifestyle) {
        return MemberLifestyle.builder()
                .member(member)
                .lifestyle(lifestyle)
                .build();
    }
}
