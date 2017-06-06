package com.simple.scheck.exception;

/**
 * Created by dell on 2017/6/1.
 */
public class BusinessException extends AbstractException{

    public BusinessException(int code, String message) {
        super(code, message);
    }

    public BusinessException(int code, Throwable cause) {
        super(code, cause);
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
