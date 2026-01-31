package com.dotoryteam.dotory.global.security.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidJwtTokenException extends ApiException {
    public InvalidJwtTokenException(HttpStatus status , String message) {
        super(status, message);
    }
}
