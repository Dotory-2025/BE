package com.dotoryteam.dotory.domain.feedback.repository;

import com.dotoryteam.dotory.domain.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
