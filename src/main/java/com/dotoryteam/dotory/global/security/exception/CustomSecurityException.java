package com.dotoryteam.dotory.global.security.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class CustomSecurityException extends ApiException {
    public CustomSecurityException(HttpStatus status , String message) {
        super(status , message);
    }
}
