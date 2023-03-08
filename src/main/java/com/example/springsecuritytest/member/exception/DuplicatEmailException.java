package com.example.springsecuritytest.member.exception;

import com.example.springsecuritytest.common.error.exception.BaseException;
import com.example.springsecuritytest.common.error.exception.ErrorCode;

public class DuplicatEmailException extends BaseException {

    public DuplicatEmailException(ErrorCode errorCode) {
        super(errorCode);
    }

}
