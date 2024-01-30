package com.ubb.userservice.common.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SessionValidationResponseDto {
    private String userId;
}
