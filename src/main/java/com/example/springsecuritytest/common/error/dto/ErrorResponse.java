package com.example.springsecuritytest.common.error.dto;

import com.example.springsecuritytest.common.error.exception.ErrorCode;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final LocalDateTime timeStamp;

    private final int status;

    private final String message;

    private final String path;

    private ErrorResponse(ErrorCode errorCode, String path) {
        this.timeStamp = LocalDateTime.now();
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.path = path;
    }

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return new ErrorResponse(errorCode, path);
    }

}
