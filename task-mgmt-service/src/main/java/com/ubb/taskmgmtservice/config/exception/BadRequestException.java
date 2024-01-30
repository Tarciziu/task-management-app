package com.ubb.taskmgmtservice.config.exception;

import com.ubb.taskmgmtservice.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
