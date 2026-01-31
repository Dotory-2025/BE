package com.dotoryteam.dotory.domain.lifestyle.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class LifestyleNotFoundException extends ApiException {
    private static final String MESSAGE = "해당 ID의 라이프스타일을 찾을 수 없습니다.";
    public LifestyleNotFoundException() {
        super(HttpStatus.NOT_FOUND , MESSAGE);
    }
}
