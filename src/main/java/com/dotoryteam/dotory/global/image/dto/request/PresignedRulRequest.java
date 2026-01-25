package com.dotoryteam.dotory.global.image.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PresignedRulRequest {
    private String prefix;
    private String originalFileName;
}
