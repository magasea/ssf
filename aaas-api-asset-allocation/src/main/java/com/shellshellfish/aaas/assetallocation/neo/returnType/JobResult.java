package com.shellshellfish.aaas.assetallocation.neo.returnType;

import org.springframework.http.HttpStatus;

/**
 * Author: hongming
 * Date: 2018/1/20
 * Desc:
 */
public class JobResult<T> {

    private int code;
    private String msg;
    private T data;

    public JobResult() {

    }

    public JobResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JobResult returnSuccess() {
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
        return this;
    }

    public JobResult returnSuccess(T data) {
        this.data = data;
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
        return this;
    }

    public JobResult returnFail() {
        this.code = -1;
        this.msg = "error";
        return this;
    }

    public JobResult returnFail(String msg) {
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
