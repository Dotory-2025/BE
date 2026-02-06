package com.dotoryteam.dotory.domain.auth.controller;

import com.dotoryteam.dotory.domain.auth.dto.request.LoginReq;
import com.dotoryteam.dotory.domain.auth.dto.response.VerifyResponse;
import com.dotoryteam.dotory.domain.auth.service.AuthService;
import com.dotoryteam.dotory.domain.member.service.MemberService;
import com.dotoryteam.dotory.global.common.dto.ApiResponse;
import com.dotoryteam.dotory.global.security.dto.JwtTokens;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<VerifyResponse>> login(@RequestBody LoginReq loginReq) {
        return ApiResponse.ok(authService.verify(
                    loginReq.getEmail() ,
                    loginReq.getAuthCode() ,
                    loginReq.getFcmToken() ,
                    loginReq.getDeviceType()
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        authService.logout(request.getHeader("Authorization"));
        return ApiResponse.ok();
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<JwtTokens>> reissue(HttpServletRequest request) {
        JwtTokens jwtTokens = authService.reissue(request.getHeader("RefreshToken"));

        return ApiResponse.ofToken(jwtTokens);
    }
}
