package com.dotoryteam.dotory.domain.alarm.repository;

import com.dotoryteam.dotory.domain.alarm.dto.response.NotificationResponse;
import com.dotoryteam.dotory.domain.alarm.entity.Notification;
import com.dotoryteam.dotory.global.common.dto.CursorResult;
import org.springframework.data.domain.Slice;

import org.springframework.data.domain.Pageable;

public interface NotificationRepositoryCustom {
    CursorResult<NotificationResponse> findNotificationsByCursor(Long memberId, String cursorId , int size);
}
