package com.dotoryteam.dotory.domain.member.entity;

import com.dotoryteam.dotory.domain.alarm.entity.FcmToken;
import com.dotoryteam.dotory.domain.auth.entity.EmailVerification;
import com.dotoryteam.dotory.domain.lifestyle.entity.MemberLifestyle;
import com.dotoryteam.dotory.domain.member.enums.UserStatus;
import com.dotoryteam.dotory.domain.term.entity.MemberTerms;
import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member") // indexes 필요 시 추가할 예정, 미리 정렬해 두는게 필요할까?
@DynamicInsert  //  기본 값 넣기 위해
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //선호 기숙사

    //이메일 인증
    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL , orphanRemoval = true)
    private EmailVerification emailVerification;

    //약관 동의
    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    private MemberTerms memberTerms;

    //회원 생활 패턴
    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    private MemberLifestyle memberLifestyle;

    //토큰
    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL , orphanRemoval = true)
    private FcmToken fcmToken;

    @Column(nullable = false , length = 20)
    private String nickname;

    @Column(name = "entrance_year" , nullable = false)
    private Integer entranceYear;


    @Column(nullable = false , length = 1)
    private String sex;

    //인앱 알림 동의 여부
    @Column(name = "notification_setting" , nullable = false)
    @ColumnDefault("true")
    private Boolean notificationSetting;

    @Column(name = "profile_img_url" , length = 250 , nullable = false)
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    @ColumnDefault("'USER'")
    private UserStatus userStatus;

    @Column(name = "feedback_score")
    @ColumnDefault("50")
    private Integer feedbackScore = 50;

    @Column(name = "match_count")
    @ColumnDefault("0")
    private Integer matchCount = 0;

    @Column(name = "member_key" , nullable = false , updatable = false , unique = true)
    private String memberKey;

    @Builder
    public Member(
            /*Dormitory dormitory*/
            String nickname ,
            int entranceYear ,
            String sex ,
            boolean notificationSetting ,
            String profileImgUrl) {
//        this.dormitory = dormitory;
        this.nickname = nickname;
        this.entranceYear = entranceYear;
        this.sex = sex;
        this.profileImgUrl = profileImgUrl;
        this.notificationSetting = notificationSetting;

        //생성 시 기본값들 설정
        this.memberKey = UUID.randomUUID().toString();
        this.userStatus = UserStatus.USER;
        this.feedbackScore = 50;
        this.matchCount = 0;
    }

    //광고 수신 동의
    public void toggleNotification(boolean isActive) {
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
