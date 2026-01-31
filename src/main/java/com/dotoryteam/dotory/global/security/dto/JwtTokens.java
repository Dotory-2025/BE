package com.dotoryteam.dotory.global.security.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokens {
    private String type;
    private String accessToken;
    private String refreshToken;
}
