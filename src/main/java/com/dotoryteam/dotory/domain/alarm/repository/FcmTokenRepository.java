package com.dotoryteam.dotory.domain.alarm.repository;

import com.dotoryteam.dotory.domain.alarm.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken , Long> , FcmTokenRepositoryCustom {
}
