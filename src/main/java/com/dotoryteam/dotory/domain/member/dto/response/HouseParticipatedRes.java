package com.dotoryteam.dotory.domain.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HouseParticipatedRes {
    private String title;
    private String livedSemester;
    private String dormitory;
    private List<String> houseConditions;
    private Boolean isRecruiting;   //  모집중

    @QueryProjection
    public HouseParticipatedRes(String title, String livedSemester , String dormitory, List<String> houseConditions, Boolean isRecruiting) {
        this.title = title;
        this.livedSemester = livedSemester;
        this.dormitory = dormitory;
        this.houseConditions = houseConditions;
        this.isRecruiting = isRecruiting;
    }
}
