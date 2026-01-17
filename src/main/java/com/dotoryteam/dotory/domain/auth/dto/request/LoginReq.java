package com.dotoryteam.dotory.domain.auth.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginReq {
    private String email;
    private String authCode;
}
