package com.dotoryteam.dotory.domain.member.enums;

import com.dotoryteam.dotory.domain.member.exception.InvalidUserStatusException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
@Getter
@RequiredArgsConstructor
public enum UserStatus {
    BANNED("banned") ,        //  신고로 계정 차단을 해야 하는 유저
    USER("user") ,          //  일반 사용자
    NEED_CHECK("needCheck");      //  신고로 계정 차단을 해야 하는지 검증할 필요가 있는 유저

    private final String status;

    public static UserStatus of(String status) {
        return Arrays.stream(values())
                .filter(dir -> dir.status.equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(InvalidUserStatusException::new);
    }
}
