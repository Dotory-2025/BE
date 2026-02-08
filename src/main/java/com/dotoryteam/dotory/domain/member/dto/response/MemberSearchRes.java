package com.dotoryteam.dotory.domain.member.dto.response;

import com.dotoryteam.dotory.domain.lifestyle.entity.MemberLifestyle;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class MemberSearchRes {
    private String hashedId;
    private UUID memberKey;
    private String nickname;
    //선호 기숙사
    private List<MemberLifestyle> lifestyles;
    private String profileImgUrl;

    @QueryProjection
    public MemberSearchRes(String hashedId , UUID memberKey , String nickname , List<MemberLifestyle> lifestyles, String profileImgUrl) {
        this.hashedId = hashedId;
        this.memberKey = memberKey;
        this.nickname = nickname;
        //선호 기숙사
        this.lifestyles = lifestyles;
        this.profileImgUrl = profileImgUrl;
    }
}
