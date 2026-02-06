package com.dotoryteam.dotory.global.image.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ExtensionEmptyException extends ApiException {
    private static final String MESSAGE = "파일 확장자는 비어있을 수 없습니다.";
    public ExtensionEmptyException() {
        super(HttpStatus.BAD_REQUEST , MESSAGE);
    }
}
