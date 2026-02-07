package com.dotoryteam.dotory.domain.member.entity;

import com.dotoryteam.dotory.domain.auth.entity.EmailVerification;
import com.dotoryteam.dotory.domain.dormitory.entity.Dormitory;
import com.dotoryteam.dotory.domain.lifestyle.entity.Lifestyle;
import com.dotoryteam.dotory.domain.lifestyle.entity.MemberLifestyle;
import com.dotoryteam.dotory.domain.member.enums.Gender;
import com.dotoryteam.dotory.domain.member.enums.UserStatus;
import com.dotoryteam.dotory.global.common.BaseEntity;
import com.dotoryteam.dotory.global.security.enums.UserRole;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Dormitory dormitory;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Column(name = "feedback_score" , nullable = false)
    private Integer feedbackScore;

    @Column(name = "match_count" , nullable = false)
    private Integer matchCount;

    @Column(name = "member_key" , nullable = false , updatable = false , unique = true)
    private UUID memberKey;

    @Builder
    public Member(
            Dormitory dormitory ,
            String nickname ,
            int entranceYear ,
            Gender sex ,
            String email ,
            List<Lifestyle> lifestyles ,
            String profileImgUrl ,
            UserRole userRole) {
        this.dormitory = dormitory;
        this.nickname = nickname;
        this.entranceYear = entranceYear;
        this.sex = sex;
        this.emailVerification = EmailVerification.builder().email(email).build();
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

        if (userRole == null) this.userRole = UserRole.USER;
        else this.userRole = userRole;

        this.feedbackScore = 50;
        this.matchCount = 0;
    }

    public void toggleNotification() {
        this.notificationSetting = !this.notificationSetting;
    }

    public void updateProfile(
            String nickname ,
            String profileImgUrl
    ) {
        if (nickname != null) this.nickname = nickname;
        if (profileImgUrl != null) this.profileImgUrl = profileImgUrl;
    }

    public void updateMemberLifestyles(List<Lifestyle> lifestyles) {
        //없는 애들만 골라서 삭제
        this.memberLifestyle.removeIf(ml -> !lifestyles.contains(ml.getLifestyle()));

        for (Lifestyle lifestyle : lifestyles) {
            // 없으면 새로 만들어서 추가
            if (!this.memberLifestyle.stream()
                    .anyMatch(ml -> ml.getLifestyle().equals(lifestyle))) {
                this.memberLifestyle.add(MemberLifestyle.of(this, lifestyle));
            }
        }
    }

    public void updateDormitory(String dormitory) {
        //나중에 dormitory repository 만들어지면 작성
    }

    public void switchToAdmin() {
        this.userRole = UserRole.ADMIN;
    }

    public void changeUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}
