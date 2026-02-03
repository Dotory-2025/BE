package com.dotoryteam.dotory.domain.member.dto.response;

import com.dotoryteam.dotory.domain.feedback.dto.response.FeedbackSessionRes;
import com.dotoryteam.dotory.domain.feedback.dto.response.MyFeedbackResultsRes;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberDetailRes {
    private String nickname;
    private String profileImgUrl;
    private String favorDormitoryName;
    private Integer entranceYear;
    private List<String> lifestyles;
    private Integer matchCount;
    private Integer feedbackScore;
    private List<MyFeedbackResultsRes> feedbackResults;
    private List<HouseParticipatedRes> memberHouse;
    //내가 쓴 피드백들
    private List<FeedbackSessionRes> feedbackIWrote;
    //내가 쓴 (내가 작성해서 방장으로 있는) 글
    private List<HouseParticipatedRes> houseIOwned;
}

