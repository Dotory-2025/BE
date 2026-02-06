package com.dotoryteam.dotory.domain.house.entity;

// TODO: Member 엔티티 생성 후 주석 해제
// import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "house_member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "house_id"})
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "house_member_id")
    private Long id;

    // TODO: Member 엔티티 생성 후 아래 주석 해제하고 memberId 필드 삭제
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "member_id", nullable = false)
    // private Member member;

    @Column(name = "member_id", nullable = false)
    private Long memberId; // 임시로 ID만 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @Column(name = "is_owner")
    @ColumnDefault("false")
    private Boolean isOwner;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Builder
    public HouseMember(Long memberId, House house, Boolean isOwner, LocalDateTime joinedAt) {
        this.memberId = memberId;
        this.house = house;
        this.isOwner = isOwner != null ? isOwner : false;  // 생성자에서 기본값 설정
        this.joinedAt = joinedAt != null ? joinedAt : LocalDateTime.now();  // 생성자에서 기본값 설정
    }

    // TODO: Member 엔티티 생성 후 주석 해제
    // @Builder
    // public HouseMember(Member member, House house, Boolean isOwner, LocalDateTime joinedAt) {
    //     this.member = member;
    //     this.house = house;
    //     this.isOwner = isOwner != null ? isOwner : false;
    //     this.joinedAt = joinedAt != null ? joinedAt : LocalDateTime.now();
    // }

    // 연관관계 편의 메서드
    public void setHouse(House house) {
        if (this.house != null) {
            this.house.getHouseMembers().remove(this);
        }
        this.house = house;
        house.getHouseMembers().add(this);
    }

    // 비즈니스 메서드
    public void assignOwnership() {
        this.isOwner = true;
    }

    public void revokeOwnership() {
        this.isOwner = false;
    }

    // TODO: Member 엔티티 생성 후 주석 해제 및 수정
    // 정적 팩토리 메서드
    // public static HouseMember createMember(Member member, House house, boolean isOwner) {
    //     HouseMember houseMember = HouseMember.builder()
    //             .member(member)
    //             .house(house)
    //             .isOwner(isOwner)
    //             .joinedAt(LocalDateTime.now())
    //             .build();
    //     house.getHouseMembers().add(houseMember);
    //     return houseMember;
    // }

    // public static HouseMember createOwner(Member member, House house) {
    //     return createMember(member, house, true);
    // }
}