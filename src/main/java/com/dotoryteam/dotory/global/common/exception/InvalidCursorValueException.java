package com.dotoryteam.dotory.global.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidCursorValueException extends ApiException{
    private static final String MESSAGE = "잘못된 커서 값입니다.";
    public InvalidCursorValueException() {
        super(HttpStatus.BAD_REQUEST , MESSAGE);
    }
}
