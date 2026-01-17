package com.dotoryteam.dotory.domain.auth.controller;

import com.dotoryteam.dotory.domain.auth.dto.request.LoginReq;
import com.dotoryteam.dotory.domain.auth.dto.response.VerifyResponse;
import com.dotoryteam.dotory.domain.auth.service.AuthService;
import com.dotoryteam.dotory.domain.auth.service.EmailVerificationService;
import com.dotoryteam.dotory.global.common.dto.ApiResponse;
import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<VerifyResponse>> login(@RequestBody LoginReq loginReq) {
        return ApiResponse.ok(authService.verify(loginReq.getEmail() , loginReq.getAuthCode()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader String accessToken) {
        authService.logout(accessToken);
        return ApiResponse.ok();
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<JwtTokens>> reissue(@RequestHeader("RefreshToken") String refreshToken) {
        JwtTokens jwtTokens = authService.reissue(refreshToken);

        return ApiResponse.ofToken(jwtTokens);
    }
}
