package com.dotoryteam.dotory.domain.member.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidJoinRequestException extends ApiException {
    private static final String MESSAGE = "잘못된 가입 요청입니다. 메일 인증을 먼저 완료해주세요.";
    public InvalidJoinRequestException() {
        super(HttpStatus.BAD_REQUEST , MESSAGE);
    }
}
