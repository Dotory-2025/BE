package com.dotoryteam.dotory.domain.member.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends ApiException {
    private static final String MESSAGE = "해당 사용자를 찾을 수 없습니다.";
    public MemberNotFoundException() {
        super(HttpStatus.NOT_FOUND, MESSAGE);
    }
}
