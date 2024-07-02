package com.example.demo.exception;

import com.example.demo.exception.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MarkethingException.class)
    protected ResponseEntity<ErrorResponse> handleMarkethingException(MarkethingException e) {
        log.error("MarkethingException", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(errorResponse.getCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParsingException(HttpMessageNotReadableException e) {
        log.error("JsonParsingException", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.JSON_PARSING_FAILED.getCode())
                .message(ErrorCode.JSON_PARSING_FAILED.getDescription() + " : " + e.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(errorResponse.getCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handelOtherException(Exception e) {
        log.error("Exception", e);
        return null;
    }
}
