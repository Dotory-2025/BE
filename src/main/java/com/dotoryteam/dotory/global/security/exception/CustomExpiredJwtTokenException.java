package com.dotoryteam.dotory.global.security.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class CustomExpiredJwtTokenException extends ApiException {
    public CustomExpiredJwtTokenException(HttpStatus status , String message) {
        super(status, message);
    }
}
