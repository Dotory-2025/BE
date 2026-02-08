package com.dotoryteam.dotory.domain.alarm.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends ApiException {
    private static final String MESSAGE = "존재하지 않는 알림입니다.";
    public NotificationNotFoundException() {
        super(HttpStatus.NOT_FOUND , MESSAGE);
    }
}
