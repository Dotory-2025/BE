package com.dotoryteam.dotory.domain.member.entity;

import com.dotoryteam.dotory.domain.auth.entity.EmailVerification;
import com.dotoryteam.dotory.domain.lifestyle.entity.Lifestyle;
import com.dotoryteam.dotory.domain.lifestyle.entity.MemberLifestyle;
import com.dotoryteam.dotory.domain.member.enums.Gender;
import com.dotoryteam.dotory.domain.member.enums.UserStatus;
import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //선호 기숙사

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<MemberLifestyle> memberLifestyle = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
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
            String email ,
            List<Lifestyle> lifestyles ,
            String profileImgUrl) {
//        this.dormitory = dormitory;
        this.nickname = nickname;
        this.entranceYear = entranceYear;
        this.sex = sex;
        this.emailVerification = new EmailVerification(
                email
        );
        this.profileImgUrl = profileImgUrl;

        if (lifestyles != null) {
            for (Lifestyle lifestyle : lifestyles) {
                // MemberLifestyle 객체를 여기서 직접 생성 (this 넘기기)
                MemberLifestyle memberLifestyle = new MemberLifestyle(this , lifestyle);
                this.memberLifestyle.add(memberLifestyle);
            }
        }

        this.notificationSetting = true;
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
