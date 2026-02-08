package com.dotoryteam.dotory.domain.alarm.service;

import com.dotoryteam.dotory.domain.alarm.dto.response.NotificationResponse;
import com.dotoryteam.dotory.domain.alarm.entity.Notification;
import com.dotoryteam.dotory.domain.alarm.repository.NotificationRepository;
import com.dotoryteam.dotory.domain.alarm.exception.NotificationNotFoundException;
import com.dotoryteam.dotory.global.common.dto.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;

    // 내 알림 목록 조회
    public CursorResult<NotificationResponse> getMyNotifications(Long memberId , String cursor, int size) {
        return notificationRepository.findNotificationsByCursor(memberId , cursor , size);
    }

    // 알림 읽음 처리 (보안 검증 포함)
    @Transactional
    public void markAsRead(Long notificationId, Long currentMemberId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);

        notification.read();
    }
}
