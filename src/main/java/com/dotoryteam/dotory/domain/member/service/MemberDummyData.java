package com.dotoryteam.dotory.domain.member.service;

import com.dotoryteam.dotory.domain.feedback.dto.response.FeedbackSessionRes;
import com.dotoryteam.dotory.domain.feedback.dto.response.MyFeedbackResultsRes;
import com.dotoryteam.dotory.domain.feedback.enums.FeedbackSessionStatus;
import com.dotoryteam.dotory.domain.member.dto.response.HouseParticipatedRes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberDummyData {
    // 피드백 전체 정보 용 더미 데이터
    public List<MyFeedbackResultsRes> DummyDataForMyFeedbackResults() {
        List<MyFeedbackResultsRes> myFeedbackResults = new ArrayList<>();

        myFeedbackResults.add(
                MyFeedbackResultsRes.builder()
                        .content("FOLLOWED_AGREED_RULES")
                        .rating(10)
                        .build()
        );

        myFeedbackResults.add(
                MyFeedbackResultsRes.builder()
                        .content("CONSIDERATE_OF_OTHERS")
                        .rating(9)
                        .build()
        );

        myFeedbackResults.add(
                MyFeedbackResultsRes.builder()
                        .content("MAINTAINS_CLEANLINESS")
                        .rating(8)
                        .build()
        );

        myFeedbackResults.add(
                MyFeedbackResultsRes.builder()
                        .content("RESPECTS_MY_SPACE")
                        .rating(7)
                        .build()
        );

        myFeedbackResults.add(
                MyFeedbackResultsRes.builder()
                        .content("WILLING_TO_LIVE_TOGETHER_AGAIN")
                        .rating(0)
                        .build()
        );

        return myFeedbackResults;
    }

    // 참여 하우스 용 더미 데이터
    public List<HouseParticipatedRes> DummyDataForHouse() {
        List<HouseParticipatedRes> houseParticipatedRes = new ArrayList<>();
        houseParticipatedRes.add(
                HouseParticipatedRes.builder()
                        .title("아침형 인간들의 모임")
                        .livedSemester("2024-1학기")
                        .dormitory("세연학사")
                        .houseConditions(List.of("SNORING", "MORNING_SHOWER", "NIGHT_WORKING"))
                        .isRecruiting(true)
                        .build()
        );

        houseParticipatedRes.add(
                HouseParticipatedRes.builder()
                        .title("거지방")
                        .livedSemester("2024-2학기")
                        .dormitory("매지학사")
                        .houseConditions(List.of("SMOKING", "SNORING", "CALL_IN_ROOM"))
                        .isRecruiting(true)
                        .build()
        );

        return houseParticipatedRes;
    }

    //내 피드백
    public List<FeedbackSessionRes> DummyDataForFeedbackSession() {
        List<FeedbackSessionRes> feedbackSessionRes = new ArrayList<>();
        feedbackSessionRes.add(
                FeedbackSessionRes.builder()
                        .semester("2024-1")
                        .sessionStatus(FeedbackSessionStatus.CLOSED.toString())
                        .feedbacks(
                                List.of(
                                        FeedbackSessionRes.MyFeedbackRes.builder()
                                                .targetMemberName("박찬호")
                                                .feedbackCode(List.of("FOLLOWED_AGREED_RULES" , "CONSIDERATE_OF_OTHERS"))
                                                .build() ,
                                        FeedbackSessionRes.MyFeedbackRes.builder()
                                                .targetMemberName("양의지")
                                                .feedbackCode(List.of("MAINTAINS_CLEANLINESS" , "RESPECTS_MY_SPACE" , "WILLING_TO_LIVE_TOGETHER_AGAIN"))
                                                .build()
                                )
                        )
                        .build()
        );

        feedbackSessionRes.add(
                FeedbackSessionRes.builder()
                        .semester("2024-2")
                        .sessionStatus(FeedbackSessionStatus.CLOSED.toString())
                        .feedbacks(
                                List.of(
                                        FeedbackSessionRes.MyFeedbackRes.builder()
                                                .targetMemberName("정수빈")
                                                .feedbackCode(List.of("MAINTAINS_CLEANLINESS"))
                                                .build() ,
                                        FeedbackSessionRes.MyFeedbackRes.builder()
                                                .targetMemberName("안재석")
                                                .feedbackCode(List.of("MAINTAINS_CLEANLINESS" , "FOLLOWED_AGREED_RULES"))
                                                .build() ,
                                        FeedbackSessionRes.MyFeedbackRes.builder()
                                                .targetMemberName("오명진")
                                                .feedbackCode(List.of("MAINTAINS_CLEANLINESS" , "FOLLOWED_AGREED_RULES"))
                                                .build()
                                )
                        )
                        .build()
        );

        return feedbackSessionRes;
    }

    public List<HouseParticipatedRes> DummyDataForHouseIOwned() {
        List<HouseParticipatedRes> houseParticipatedRes = new ArrayList<>();
        houseParticipatedRes.add(
                HouseParticipatedRes.builder()
                        .title("아침형 인간들의 모임")
                        .livedSemester("2024-1학기")
                        .dormitory("세연학사")
                        .houseConditions(List.of("SNORING", "MORNING_SHOWER", "NIGHT_WORKING"))
                        .isRecruiting(false)
                        .build()
        );

        return houseParticipatedRes;
    }
}
