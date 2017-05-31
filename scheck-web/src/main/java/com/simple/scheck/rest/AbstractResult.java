package com.simple.scheck.rest;

/**
 * Created by dell on 2017/5/31.
 */
public abstract class AbstractResult {

    private String message;

    private int code;

    public AbstractResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
