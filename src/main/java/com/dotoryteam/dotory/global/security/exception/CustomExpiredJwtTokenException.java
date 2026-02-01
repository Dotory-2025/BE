package com.dotoryteam.dotory.global.security.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import com.google.api.Http;
import org.springframework.http.HttpStatus;

public class CustomExpiredJwtTokenException extends ApiException {
    private static final String MESSAGE = "만료된 토큰입니다.";
    public CustomExpiredJwtTokenException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
