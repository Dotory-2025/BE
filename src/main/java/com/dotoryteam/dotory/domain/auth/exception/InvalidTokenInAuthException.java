package com.dotoryteam.dotory.domain.auth.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidTokenInAuthException extends ApiException {
    public InvalidTokenInAuthException(HttpStatus status , String message) {
        super(status, message);
    }
}
