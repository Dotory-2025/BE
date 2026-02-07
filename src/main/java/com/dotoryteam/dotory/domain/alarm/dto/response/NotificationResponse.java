package com.dotoryteam.dotory.domain.alarm.dto.response;

import com.dotoryteam.dotory.domain.alarm.enums.NotificationType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;

    @QueryProjection
    public NotificationResponse(Long id ,
                                String message ,
                                NotificationType type ,
                                boolean isRead ,
                                LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}