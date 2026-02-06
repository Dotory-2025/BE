package com.dotoryteam.dotory.global.image.exception;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FileSizeTooBigException extends ApiException {
    private static final String MESSAGE = "파일 크기가 너무 큽니다.";
    public FileSizeTooBigException(long fileSize) {
        super(HttpStatus.BAD_REQUEST , MESSAGE + "(최대 " + fileSize / (1024 * 1024L) + "MB)");
    }
}
