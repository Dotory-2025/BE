package com.dotoryteam.dotory.domain.member.dto.request;

import com.dotoryteam.dotory.domain.dormitory.entity.Dormitory;
import com.dotoryteam.dotory.domain.member.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class JoinReq {
    private String signUpToken;
    private String email;

    private String nickname;
    private Integer entranceYear;
//    private Dormitory dormitory;
    private Gender sex;
    private List<String> lifestyleCodes;
    private String profileImgUrl;
}
