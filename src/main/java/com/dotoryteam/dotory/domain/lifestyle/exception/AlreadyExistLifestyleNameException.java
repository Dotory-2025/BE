package com.dotoryteam.dotory.domain.lifestyle.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AlreadyExistLifestyleNameException extends ApiException {
    private static final String MESSAGE = "이미 존재하는 라이프스타일 명 입니다. 다른 이름을 사용해주세요.";
    public AlreadyExistLifestyleNameException() {
        super(HttpStatus.CONFLICT , MESSAGE);
    }
}
