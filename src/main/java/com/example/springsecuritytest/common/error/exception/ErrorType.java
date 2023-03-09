package com.example.springsecuritytest.common.error.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum ErrorType {

    INVALID_INPUT_VALUE("ERR001", HttpStatus.BAD_REQUEST.value(), "유효하지 않는 입력 값입니다."),
    METHOD_NOT_ALLOWD("ERR002", HttpStatus.METHOD_NOT_ALLOWED.value(), "유효하지 않는 요청 메소드입니다."),
    DUPLICATE_EMAIL("ERR003", HttpStatus.CONTINUE.value(), "사용 중인 이메일입니다."),
    NOT_FOUND_MEMBER("ERR004", HttpStatus.BAD_REQUEST.value(), "사용자를 찾을 수 없습니다."),
    BAD_CREDENTIALS("ERR005", HttpStatus.BAD_REQUEST.value(), "이메일 또는 비밀번호를 잘못 입력했습니다.");

    private final String code;

    private final int status;

    private final String message;

    public static ErrorType findByErrorCode(String code) {
        return Arrays.stream(ErrorType.values())
                .filter(errorCode -> errorCode.getCode().equals(code))
                .findFirst()
                .orElseThrow();
    }

    ErrorType(String code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
