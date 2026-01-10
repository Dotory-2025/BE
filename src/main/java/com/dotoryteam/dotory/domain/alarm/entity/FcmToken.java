package com.dotoryteam.dotory.domain.alarm.entity;

import com.dotoryteam.dotory.domain.alarm.enums.DeviceType;
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
@Table(name = "fcm_token")
public class FcmToken extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(name = "fcm_token" , nullable = false , length = 512 , unique = true)
    private String fcmToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id" , nullable = false)
    private Member member;

    @Builder
    public FcmToken(DeviceType deviceType, String fcmToken, Member member) {
        this.deviceType = deviceType;
        this.fcmToken = fcmToken;
        this.member = member;
    }

    public void refresh(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
