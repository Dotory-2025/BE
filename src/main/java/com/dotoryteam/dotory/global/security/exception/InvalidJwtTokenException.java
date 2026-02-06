package com.dotoryteam.dotory.global.security.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidJwtTokenException extends ApiException {
    private static final String MESSAGE = "유효하지 않은 토큰입니다.";
    public InvalidJwtTokenException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
