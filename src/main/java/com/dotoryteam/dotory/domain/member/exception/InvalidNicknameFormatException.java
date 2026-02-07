package com.dotoryteam.dotory.domain.member.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidNicknameFormatException extends ApiException {
    private static final String MESSAGE = "올바른 형태의 닉네임이 아닙니다.";
    public InvalidNicknameFormatException() {
        super(HttpStatus.BAD_REQUEST , MESSAGE);
    }
}
