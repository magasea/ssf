package com.shellshellfish.aaas.common.http;

import org.springframework.http.HttpStatus;

/**
 * Author: hongming
 * Date: 2018/1/20
 * Desc:
 */
public class HttpJsonResult<T> {

    private int code;
    private String msg;
    private T data;

    public HttpJsonResult() {

    }

    public HttpJsonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public HttpJsonResult returnSuccess() {
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
        return this;
    }

    public HttpJsonResult returnSuccess(T data) {
        this.data = data;
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
        return this;
    }

    public HttpJsonResult returnFail() {
        this.code = -1;
        this.msg = "error";
        return this;
    }

    public HttpJsonResult returnFail(String msg) {
        this.code = -1;
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
