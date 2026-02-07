package com.dotoryteam.dotory.domain.alarm.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FcmMessageSendException extends ApiException {
    private static final String MESSAGE = "알림 전송에 실패하였습니다.";
    public FcmMessageSendException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
