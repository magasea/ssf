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

    public static HttpJsonResult returnSuccess() {
        HttpJsonResult result = new HttpJsonResult();
        result.setCode(HttpStatus.OK.value());
        result.setMsg(HttpStatus.OK.getReasonPhrase());
        return result;
    }

    public HttpJsonResult returnSuccess(T data) {
        HttpJsonResult result = this.returnSuccess();
        result.setData(data);
        return result;
    }

    public static HttpJsonResult returnFail() {
        HttpJsonResult result = new HttpJsonResult();
        result.setCode(HttpStatus.FAILED_DEPENDENCY.value());
        result.setMsg("failed");
        return result;
    }

    public static HttpJsonResult returnFail(String msg) {
        HttpJsonResult result = new HttpJsonResult();
        result.setCode(HttpStatus.FAILED_DEPENDENCY.value());
        result.setMsg(msg);
        return result;
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
