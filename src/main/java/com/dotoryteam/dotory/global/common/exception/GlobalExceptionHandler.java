package com.dotoryteam.dotory.global.common.exception;

import com.dotoryteam.dotory.domain.auth.exception.CustomAuthException;
import com.dotoryteam.dotory.global.common.dto.ApiResponse;
import com.dotoryteam.dotory.global.security.enums.ErrorCode;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> ResponseEntity<ApiResponse<T>> handleValidationException(
            MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("잘못된 요청입니다.");
        return ApiResponse.failedOf(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public <T> ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        ex.printStackTrace();
        return ApiResponse.failedOf(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler
    public <T> ResponseEntity<ApiResponse<Void>> handleCustomAuthException(CustomAuthException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.of(errorCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ex.printStackTrace();
        return ApiResponse.failedOf(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다.");
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException e) {
        return ApiResponse.failedOf(e.status, e.getMessage());
    }
}