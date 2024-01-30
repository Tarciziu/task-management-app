package com.ubb.taskmgmtservice.config.exception;

import com.ubb.taskmgmtservice.common.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
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
