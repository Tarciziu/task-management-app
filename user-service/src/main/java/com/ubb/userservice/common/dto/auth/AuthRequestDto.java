package com.ubb.userservice.common.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
