package com.dotoryteam.dotory.domain.invitation.entity;

import com.dotoryteam.dotory.domain.house.entity.House;
// TODO: Member 엔티티 생성 후 주석 해제
// import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.global.common.BaseEntity;
import com.dotoryteam.dotory.global.common.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"sender_member_id", "receiver_member_id", "house_id"})
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long id;

    // TODO: Member 엔티티 생성 후 아래 주석 해제하고 senderId, receiverId 필드 삭제
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "sender_member_id", nullable = false)
    // private Member sender;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "receiver_member_id", nullable = false)
    // private Member receiver;

    @Column(name = "sender_member_id", nullable = false)
    private Long senderId; // 임시로 ID만 저장

    @Column(name = "receiver_member_id", nullable = false)
    private Long receiverId; // 임시로 ID만 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @ColumnDefault("'PENDING'")
    private InvitationStatus status;

    @Column(name = "invited_at")
    private LocalDateTime invitedAt;

    @Builder
    public Invitation(Long senderId, Long receiverId, House house, InvitationStatus status, LocalDateTime invitedAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.house = house;
        this.status = status != null ? status : InvitationStatus.PENDING;  // 생성자에서 기본값 설정
        this.invitedAt = invitedAt != null ? invitedAt : LocalDateTime.now();  // 생성자에서 기본값 설정
    }

    // TODO: Member 엔티티 생성 후 주석 해제
    // @Builder
    // public Invitation(Member sender, Member receiver, House house, InvitationStatus status, LocalDateTime invitedAt) {
    //     this.sender = sender;
    //     this.receiver = receiver;
    //     this.house = house;
    //     this.status = status != null ? status : InvitationStatus.PENDING;
    //     this.invitedAt = invitedAt != null ? invitedAt : LocalDateTime.now();
    // }

    // 비즈니스 메서드
    public void accept() {
        if (this.status != InvitationStatus.PENDING) {
            throw new IllegalStateException("초대는 PENDING 상태에서만 수락할 수 있습니다.");
        }
        this.status = InvitationStatus.ACCEPTED;
    }

    public void reject() {
        if (this.status != InvitationStatus.PENDING) {
            throw new IllegalStateException("초대는 PENDING 상태에서만 거절할 수 있습니다.");
        }
        this.status = InvitationStatus.REJECTED;
    }

    public void cancel() {
        if (this.status != InvitationStatus.PENDING) {
            throw new IllegalStateException("초대는 PENDING 상태에서만 취소할 수 있습니다.");
        }
        this.status = InvitationStatus.CANCELLED;
    }

    public boolean isPending() {
        return this.status == InvitationStatus.PENDING;
    }

    public boolean isAccepted() {
        return this.status == InvitationStatus.ACCEPTED;
    }

    // TODO: Member 엔티티 생성 후 주석 해제 및 수정
    // 정적 팩토리 메서드
    // public static Invitation create(Member sender, Member receiver, House house) {
    //     return Invitation.builder()
    //             .sender(sender)
    //             .receiver(receiver)
    //             .house(house)
    //             .status(InvitationStatus.PENDING)
    //             .invitedAt(LocalDateTime.now())
    //             .build();
    // }
}