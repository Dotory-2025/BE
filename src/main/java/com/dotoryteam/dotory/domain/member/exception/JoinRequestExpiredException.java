package com.dotoryteam.dotory.domain.member.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class JoinRequestExpiredException extends ApiException {
    private static final String MESSAGE = "잘못된 회원가입 인증 요청입니다.";
    public JoinRequestExpiredException() {
        super(HttpStatus.BAD_REQUEST , MESSAGE);
    }
}
