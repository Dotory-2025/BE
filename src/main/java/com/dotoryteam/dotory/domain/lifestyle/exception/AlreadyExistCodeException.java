package com.dotoryteam.dotory.domain.lifestyle.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AlreadyExistCodeException extends ApiException {
    private static final String MESSAGE = "이미 존재하는 코드입니다. 다른 코드를 사용해주세요";
    public AlreadyExistCodeException() {
        super(HttpStatus.CONFLICT , MESSAGE);
    }
}
