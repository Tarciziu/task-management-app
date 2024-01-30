package com.ubb.userservice.config.exception;

import com.ubb.userservice.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
