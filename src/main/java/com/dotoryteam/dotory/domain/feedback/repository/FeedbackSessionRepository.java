package com.dotoryteam.dotory.domain.feedback.repository;

import com.dotoryteam.dotory.domain.feedback.entity.FeedbackSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackSessionRepository extends JpaRepository<FeedbackSession, Long> {
}
