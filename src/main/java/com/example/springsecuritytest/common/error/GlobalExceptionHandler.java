package com.example.springsecuritytest.common.error;

import com.example.springsecuritytest.common.error.dto.ErrorResponse;
import com.example.springsecuritytest.common.error.exception.BaseException;

import com.example.springsecuritytest.common.error.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        ErrorResponse response = ErrorResponse.of(e.getErrorCode(), path);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request) {
        String path = request.getRequestURI();
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, path);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpServletRequest request) {
        String path = request.getRequestURI();
        ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWD, path);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
