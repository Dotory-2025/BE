package com.dotoryteam.dotory.domain.member.entity;

import com.dotoryteam.dotory.domain.auth.entity.EmailVerification;
import com.dotoryteam.dotory.domain.member.enums.Gender;
import com.dotoryteam.dotory.domain.member.enums.UserStatus;
import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //선호 기숙사

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_verification_id", nullable = false , unique = true)
    private EmailVerification emailVerification;

    @Column(nullable = false , length = 20)
    private String nickname;

    @Column(name = "entrance_year" , nullable = false)
    private Integer entranceYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender sex;

    //인앱 알림 동의 여부
    @Column(name = "notification_setting" , nullable = false)
    private Boolean notificationSetting;

    @Column(name = "profile_img_url" , length = 250 , nullable = false)
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Column(name = "feedback_score" , nullable = false)
    private Integer feedbackScore;

    @Column(name = "match_count" , nullable = false)
    private Integer matchCount;

    @Column(name = "member_key" , nullable = false , updatable = false , unique = true)
    private UUID memberKey;

    @Builder
    public Member(
            /*Dormitory dormitory*/
            String nickname ,
            int entranceYear ,
            Gender sex ,
            boolean notificationSetting ,
            String profileImgUrl) {
//        this.dormitory = dormitory;
        this.nickname = nickname;
        this.entranceYear = entranceYear;
        this.sex = sex;
        this.profileImgUrl = profileImgUrl;
        this.notificationSetting = notificationSetting;
        this.memberKey = UUID.randomUUID();
        this.userStatus = UserStatus.USER;
        this.feedbackScore = 50;
        this.matchCount = 0;
    }

    public void toggleNotification() {
        this.notificationSetting = !this.notificationSetting;
    }

    public void updateProfile(String nickname , String profileImgUrl) {
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
    }

    public void changeUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}
