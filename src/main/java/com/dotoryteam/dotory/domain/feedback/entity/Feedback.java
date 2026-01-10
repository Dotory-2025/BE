package com.dotoryteam.dotory.domain.feedback.entity;

import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //하우스 멤버 엔티티 연결부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_session_id" , nullable = false)
    private FeedbackSession session;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "feedback_semester", nullable = false)
    private String feedbackSemester;

    @Builder
    public Feedback(FeedbackSession session, Integer rating, String feedbackSemester) {
        this.session = session;
        this.rating = rating;
        this.feedbackSemester = feedbackSemester;
    }

    public void updateRating(Integer rating) {
        this.rating = rating;
    }
}
