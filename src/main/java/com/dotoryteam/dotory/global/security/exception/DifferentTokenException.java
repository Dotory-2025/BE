package com.dotoryteam.dotory.global.security.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DifferentTokenException extends ApiException {
    private static final String MESSAGE = "토큰 정보가 일치하지 않습니다.";
    public DifferentTokenException() {
        super(HttpStatus.BAD_REQUEST , MESSAGE);
    }
}
