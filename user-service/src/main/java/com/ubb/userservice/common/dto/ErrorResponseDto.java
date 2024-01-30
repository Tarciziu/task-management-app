package com.ubb.userservice.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ErrorResponseDto {
    private int status;
    private String code;
    private String message;
    private List<ErrorResponseItemDto> additionalDetails;

    @Setter
    @Getter
    public static class ErrorResponseItemDto {
        private String fieldName;
        private String error;
    }
}
