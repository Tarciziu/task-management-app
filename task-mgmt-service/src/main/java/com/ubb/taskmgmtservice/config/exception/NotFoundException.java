package com.ubb.taskmgmtservice.config.exception;

import com.ubb.taskmgmtservice.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
