package com.dotoryteam.dotory.domain.auth.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidTokenInAuthException extends ApiException {
    private static final String MESSAGE = "유효하지 않은 토큰입니다.";
    public InvalidTokenInAuthException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
