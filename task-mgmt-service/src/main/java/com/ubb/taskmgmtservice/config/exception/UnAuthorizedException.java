package com.ubb.taskmgmtservice.config.exception;

import com.ubb.taskmgmtservice.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends BaseException {
    public UnAuthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
