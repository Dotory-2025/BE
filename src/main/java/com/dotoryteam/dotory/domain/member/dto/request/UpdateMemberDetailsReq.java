package com.dotoryteam.dotory.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateMemberDetailsReq {
    private String nickname;
    private String profilePictureUrl;
    private List<String> lifestyleCodes;
    private String dormitoryCode;
}
