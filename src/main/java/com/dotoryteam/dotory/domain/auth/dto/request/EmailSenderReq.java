package com.dotoryteam.dotory.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailSenderReq {
    @NotBlank @Email
    private String email;
}
