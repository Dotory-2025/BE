package com.dotoryteam.dotory.domain.auth.dto.response;

import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerifyResponse {
    private String signupToken;
    private boolean isMember;
    private JwtTokens jwtTokens;
}
