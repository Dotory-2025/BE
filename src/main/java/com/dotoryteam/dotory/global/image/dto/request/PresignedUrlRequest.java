package com.dotoryteam.dotory.global.image.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PresignedUrlRequest {
    @NotBlank(message = "디렉토리명은 비어있을 수 없습니다.") private String prefix;
    @NotBlank(message = "파일명은 비어있을 수 없습니다.") private String originalFileName;
    @NotBlank(message = "파일 크기는 0보다 커야 합니다.") private Long fileSize;
}
