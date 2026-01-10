package com.dotoryteam.dotory.domain.feedback.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feedback_content")
public class FeedbackContent {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false , length = 512)
    private String content;

    @Builder
    public FeedbackContent(String content) {
        this.content = content;
    }
}
