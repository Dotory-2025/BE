package com.dotoryteam.dotory.domain.lifestyle.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LifestyleManageDTO {
    private Long id;
    private String lifestyleName;
    private String code;
}
