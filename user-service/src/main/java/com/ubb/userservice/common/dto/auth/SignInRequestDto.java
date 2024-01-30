package com.ubb.userservice.common.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInRequestDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    @NotBlank
    private String password;
}
