package com.ubb.userservice.config.exception;

import com.ubb.userservice.common.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public abstract class BaseException extends RuntimeException {
    private ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    abstract HttpStatus getStatus();
}
