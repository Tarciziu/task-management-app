package com.ubb.userservice.config.exception;

import com.ubb.userservice.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ServerException extends BaseException {
    public ServerException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
