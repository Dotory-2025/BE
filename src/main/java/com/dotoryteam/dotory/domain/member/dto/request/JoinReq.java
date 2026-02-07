package com.dotoryteam.dotory.domain.member.dto.request;

import com.dotoryteam.dotory.domain.dormitory.entity.Dormitory;
import com.dotoryteam.dotory.domain.member.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class JoinReq {
    @NotBlank(message = "가입 여부 토큰은 필수값입니다.") private String signUpToken;
    @NotBlank(message = "이메일은 필수값입니다.") private String email;

    @NotBlank(message = "닉네임은 필수값입니다.") private String nickname;
    @NotBlank(message = "입학 년도는 필수값입니다.") private Integer entranceYear;
//  @NotBlank(message = "")  private Dormitory dormitory;
    @NotBlank(message = "성별은 필수값입니다.") private Gender sex;
    @NotBlank(message = "생활 패턴은 반드시 하나 이상 기입해야 합니다.") private List<String> lifestyleCodes;
    @NotBlank(message = "프로필 이미지는 비어있을 수 없습니다.") private String profileImgUrl;
}
