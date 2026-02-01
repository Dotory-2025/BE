package com.dotoryteam.dotory.domain.auth.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class MailSenderException extends ApiException {
    private static final String MESSAGE = "메일 전송에 실패했습니다.";
    public MailSenderException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR , MESSAGE);
    }
}
