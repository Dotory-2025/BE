package com.dotoryteam.dotory.domain.member.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AlreadyExistNicknameException extends ApiException {
    private static final String MESSAGE = "이미 사용중인 닉네임입니다.";
    public AlreadyExistNicknameException() {
        super(HttpStatus.CONFLICT, MESSAGE);
    }
}
