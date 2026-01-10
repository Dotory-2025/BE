package com.dotoryteam.dotory.domain.member.enums;

public enum UserStatus {
    BANNED ,        //  신고로 계정 차단을 해야 하는 유저
    USER ,          //  일반 사용자
    NEED_CHECK      //  신고로 계정 차단을 해야 하는지 검증할 필요가 있는 유저
}
