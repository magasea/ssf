package com.shellshellfish.aaas.userinfo.model.dto;

public class Error {
    private int code=0;
    private String message;

    public Error(String message) {
    	this.message=message;
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