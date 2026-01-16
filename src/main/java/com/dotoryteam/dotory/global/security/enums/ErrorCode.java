package com.dotoryteam.dotory.global.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 BAD_REQUEST: 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST , "인증번호가 일치하지 않습니다."),
    EXPIRED_AUTH_CODE(HttpStatus.BAD_REQUEST , "인증번호가 만료되었습니다."),

    // 401 UNAUTHORIZED: 인증 실패
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    LOGOUT_TOKEN(HttpStatus.UNAUTHORIZED, "로그아웃된 토큰입니다. 다시 로그인해주세요."),

    // 403 FORBIDDEN: 권한 없음
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 404 NOT_FOUND: 리소스를 찾을 수 없음
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),

    // 500 INTERNAL_SERVER_ERROR: 서버 에러
    MAIL_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR , "메일 전송에 실패했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러입니다.");

    private final HttpStatus status;
    private final String message;
}
