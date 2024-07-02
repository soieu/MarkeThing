package com.example.demo.exception;

import com.example.demo.exception.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MarkethingException extends RuntimeException {
    private ErrorCode errorCode;

    public int getStatusCode() {
        return errorCode.getCode();
    }

    public String getMessage() {
        return errorCode.getDescription();
    }
}
