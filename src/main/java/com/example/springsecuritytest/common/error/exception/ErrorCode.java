package com.example.springsecuritytest.common.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "유효하지 않는 입력 값입니다."),
    METHOD_NOT_ALLOWD(HttpStatus.METHOD_NOT_ALLOWED.value(), "유효하지 않는 요청 메소드입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONTINUE.value(), "사용 중인 이메일입니다.");

    private final int status;

    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
