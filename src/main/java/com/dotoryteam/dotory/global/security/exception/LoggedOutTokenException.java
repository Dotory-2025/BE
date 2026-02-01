package com.dotoryteam.dotory.global.security.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class LoggedOutTokenException extends ApiException {
    private static final String MESSAGE = "로그아웃된 토큰입니다.";
    public LoggedOutTokenException() {
        super(HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
