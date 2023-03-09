package com.example.springsecuritytest.common.error.dto;

import com.example.springsecuritytest.common.error.exception.ErrorType;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final LocalDateTime timeStamp;

    private final int status;

    private final String message;

    private final String path;

    private ErrorResponse(ErrorType errorType, String path) {
        this.timeStamp = LocalDateTime.now();
        this.status = errorType.getStatus();
        this.message = errorType.getMessage();
        this.path = path;
    }

    public static ErrorResponse of(ErrorType errorType, String path) {
        return new ErrorResponse(errorType, path);
    }

}
