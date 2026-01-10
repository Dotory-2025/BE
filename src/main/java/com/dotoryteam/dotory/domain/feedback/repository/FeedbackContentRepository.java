package com.dotoryteam.dotory.domain.feedback.repository;

import com.dotoryteam.dotory.domain.feedback.entity.FeedbackContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackContentRepository extends JpaRepository<FeedbackContent, Long> {
}
