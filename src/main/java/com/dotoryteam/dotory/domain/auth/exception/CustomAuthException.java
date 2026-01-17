package com.dotoryteam.dotory.domain.auth.exception;

import com.dotoryteam.dotory.global.security.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomAuthException extends RuntimeException {
    private final ErrorCode errorCode;
}
