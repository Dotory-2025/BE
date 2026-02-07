package com.dotoryteam.dotory.domain.alarm.dto.request;

import com.dotoryteam.dotory.domain.alarm.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.UUID;

@Getter
@Builder
public class NotificationEvent {
    private UUID memberUuid;
    private NotificationType notificationType;
    private String title;
    private String message;
    @Nullable
    private String data;

    public static NotificationEvent of(UUID memberUuid , NotificationType type , String title , String message , String data) {
        return NotificationEvent.builder()
                .memberUuid(memberUuid)
                .notificationType(type)
                .title(title)
                .message(message)
                .data(data)
                .build();
    }
}
