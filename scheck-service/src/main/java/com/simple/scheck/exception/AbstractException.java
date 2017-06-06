package com.simple.scheck.exception;

/**
 * Created by dell on 2017/6/1.
 */
public abstract class AbstractException extends Exception{

    private int code;

    public AbstractException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AbstractException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public AbstractException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
