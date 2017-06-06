package com.simple.scheck.rest;

import com.simple.scheck.exception.StatusCode;

/**
 * Created by dell on 2017/5/31.
 */
public class Result<T> extends AbstractResult{

    private T data;

    public Result(int code, String message) {
        super(code, message);
    }

    public static <T> Result<T> ok() {return new Result<>(StatusCode.SUCCESS, null);}

    public static <T> Result<T> ok(int code, String message) {return new Result<>(code, message);}

    public static <T> Result<T> error() {return new Result<>(StatusCode.ERROR, null);}

    public static <T> Result<T> error(int code, String message) {return new Result<>(code, message);}

    public static <T> Result<T> one(T obj) {
        Result<T> result = new Result(StatusCode.SUCCESS, null);
        result.setData(obj);
        return result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
