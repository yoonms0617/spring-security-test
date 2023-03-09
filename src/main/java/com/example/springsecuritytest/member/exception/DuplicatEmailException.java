package com.example.springsecuritytest.member.exception;

import com.example.springsecuritytest.common.error.exception.BaseException;
import com.example.springsecuritytest.common.error.exception.ErrorType;

public class DuplicatEmailException extends BaseException {

    public DuplicatEmailException(ErrorType errorType) {
        super(errorType);
    }

}
