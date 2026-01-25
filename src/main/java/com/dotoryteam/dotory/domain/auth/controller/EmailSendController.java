package com.dotoryteam.dotory.domain.auth.controller;

import com.dotoryteam.dotory.domain.auth.dto.request.EmailSenderReq;
import com.dotoryteam.dotory.domain.auth.service.EmailVerificationService;
import com.dotoryteam.dotory.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email-verification")
public class EmailSendController {
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Void>> sendEmail(@RequestBody EmailSenderReq emailSenderReq) {
        emailVerificationService.sendVerificationCode(emailSenderReq.getEmail());
        return ApiResponse.ok();
    }
}
