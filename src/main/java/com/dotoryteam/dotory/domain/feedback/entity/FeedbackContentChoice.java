package com.dotoryteam.dotory.domain.feedback.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feedback_content_choice")
public class FeedbackContentChoice {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id" , nullable = false)
    private Feedback feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_content_id" , nullable = false)
    private FeedbackContent feedbackContent;

    @Builder
    public FeedbackContentChoice(Feedback feedback, FeedbackContent feedbackContent) {
        this.feedback = feedback;
        this.feedbackContent = feedbackContent;
    }
}
