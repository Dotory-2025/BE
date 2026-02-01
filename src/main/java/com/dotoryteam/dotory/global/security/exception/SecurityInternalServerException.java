package com.dotoryteam.dotory.global.security.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SecurityInternalServerException extends ApiException {
    private static final String MESSAGE = "서버 내부 오류입니다.";
    public SecurityInternalServerException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR , MESSAGE);
    }
}
