package com.scu.exception;

/**
 * 账号不存在异常
 */
public class InvalidRegisterInfoException extends BaseException {


    public InvalidRegisterInfoException(String message) {
        super(message);
    }
}
