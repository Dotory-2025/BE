package com.dotoryteam.dotory.domain.feedback.repository;

import com.dotoryteam.dotory.domain.feedback.entity.FeedbackContentChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackContentChoiceRepository extends JpaRepository<FeedbackContentChoice, Long> {
}
