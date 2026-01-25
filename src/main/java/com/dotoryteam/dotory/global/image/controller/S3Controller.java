package com.dotoryteam.dotory.global.image.controller;

import com.dotoryteam.dotory.global.common.dto.ApiResponse;
import com.dotoryteam.dotory.global.image.dto.request.PresignedRulRequest;
import com.dotoryteam.dotory.global.image.dto.response.PresignedUrlResponse;
import com.dotoryteam.dotory.global.image.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/presigned-url")
    public ResponseEntity<ApiResponse<PresignedUrlResponse>> getPresignedUrl(@RequestBody PresignedRulRequest request) {
        return ApiResponse.ok(
                s3Service.getPresignedUrl(
                        request.getPrefix() ,
                        request.getOriginalFileName()
                )
        );
    }
}
