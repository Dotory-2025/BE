package com.dotoryteam.dotory.global.security.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class LoggedOutTokenException extends ApiException {
    public LoggedOutTokenException() {
        super(HttpStatus.UNAUTHORIZED, "로그아웃 된 토큰입니다.");
    }
}
