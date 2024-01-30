package com.ubb.taskmgmtservice.config.exception;

import com.ubb.taskmgmtservice.common.dto.ErrorResponseDto;
import com.ubb.taskmgmtservice.common.enums.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.ubb.taskmgmtservice.common.enums.ErrorCode.BAD_REQUEST;

@RestControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDto> parseBusinessException(BaseException ex) {
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(ex.getStatus().value());
        errorResponseDto.setCode(ex.getErrorCode().getCode());
        errorResponseDto.setMessage(ex.getErrorCode().getMessage());

        return new ResponseEntity<>(errorResponseDto, ex.getStatus());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponseDto> parseBusinessException(Throwable ex) {
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponseDto.setCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        errorResponseDto.setMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleTypeMismatchException(MethodArgumentTypeMismatchException mismatchException) {
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setCode(BAD_REQUEST.getCode());
        errorResponseDto.setMessage(mismatchException.getMessage());

        return errorResponseDto;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMessageNotReadableException(HttpMessageNotReadableException messageNotReadableException) {
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setCode(BAD_REQUEST.getCode());
        errorResponseDto.setMessage(messageNotReadableException.getMessage());

        return errorResponseDto;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleConstraintViolationException(MethodArgumentNotValidException constraintViolationException) {
        final var additionalDetails = constraintViolationException.getBindingResult().getFieldErrors()
                .stream()
                .map(constraint -> {
                    final String defaultMessage = constraint.getDefaultMessage();
                    final var errorItem = new ErrorResponseDto.ErrorResponseItemDto();
                    errorItem.setFieldName(constraint.getField());
                    errorItem.setError(defaultMessage);

                    return errorItem;
                }).toList();

        final ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setCode(BAD_REQUEST.getCode());
        errorResponseDto.setMessage(BAD_REQUEST.getMessage());
        errorResponseDto.setAdditionalDetails(additionalDetails);

        return errorResponseDto;
    }
}
