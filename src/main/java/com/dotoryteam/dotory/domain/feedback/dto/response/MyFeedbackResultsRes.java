package com.dotoryteam.dotory.domain.feedback.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyFeedbackResultsRes {
    private String content;
    private Integer rating;

    @QueryProjection
    public MyFeedbackResultsRes(String content , Integer rating) {
        this.content = content;
        this.rating = rating;
    }
}
