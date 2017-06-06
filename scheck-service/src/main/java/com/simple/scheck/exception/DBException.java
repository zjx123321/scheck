package com.simple.scheck.exception;

/**
 * Created by dell on 2017/6/1.
 */
public class DBException extends AbstractException{

    public DBException(int code, String message) {
        super(code, message);
    }

    public DBException(int code, Throwable cause) {
        super(code, cause);
    }

    public DBException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
