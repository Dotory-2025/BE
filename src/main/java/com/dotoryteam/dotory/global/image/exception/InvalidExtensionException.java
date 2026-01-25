package com.dotoryteam.dotory.global.image.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidExtensionException extends ApiException {
    public InvalidExtensionException(HttpStatus status , String message) {
        super(status, message);
    }
}
