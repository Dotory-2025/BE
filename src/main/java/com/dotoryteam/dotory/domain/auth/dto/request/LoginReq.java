package com.dotoryteam.dotory.domain.auth.dto.request;

import com.dotoryteam.dotory.domain.alarm.enums.DeviceType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginReq {
    private String email;
    private String authCode;
    private String fcmToken;
    private DeviceType deviceType;
}
