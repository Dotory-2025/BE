package com.dotoryteam.dotory.domain.feedback.entity;

import com.dotoryteam.dotory.domain.feedback.enums.FeedbackSessionStatus;
import com.dotoryteam.dotory.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feedback_session")
public class FeedbackSession extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //하우스 엔티티 연결부

    @Enumerated(EnumType.STRING)
    @Column(name = "session_status" , nullable = false)
    private FeedbackSessionStatus sessionStatus;

    @Builder
    public FeedbackSession(FeedbackSessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public void close() {
        this.sessionStatus = FeedbackSessionStatus.CLOSED;
    }
}
