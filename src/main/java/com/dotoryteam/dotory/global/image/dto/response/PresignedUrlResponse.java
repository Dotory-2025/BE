package com.dotoryteam.dotory.global.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PresignedUrlResponse {
    private final String presignedUrl;
    private final String key;
}
