package com.dotoryteam.dotory.domain.member.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidUserStatusException extends ApiException {
    private static final String MESSAGE = "잘못된 사용자 상태 요청입니다.";
    public InvalidUserStatusException() {
        super(HttpStatus.BAD_REQUEST , MESSAGE);
    }
}
