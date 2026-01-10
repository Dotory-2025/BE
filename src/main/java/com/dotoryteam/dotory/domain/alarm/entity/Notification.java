package com.dotoryteam.dotory.domain.alarm.entity;

import com.dotoryteam.dotory.domain.alarm.enums.NotificationType;
import com.dotoryteam.dotory.domain.member.entity.Member;
import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id" , nullable = false)
    private Member member;

    @Column(name = "notification_message" , nullable = false , length = 255)
    private String message;

    @Column(name = "is_read" , nullable = false)
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type" , nullable = false)
    private NotificationType notificationType;

    @Column(name = "target_id")
    private Long targetId;

    @Builder
    public Notification(Member member, String message, NotificationType notificationType, Long targetId) {
        this.member = member;
        this.message = message;
        this.isRead = false;
        this.notificationType = notificationType;
        this.targetId = targetId;
    }

    public void read() {
        this.isRead = true;
    }
}
