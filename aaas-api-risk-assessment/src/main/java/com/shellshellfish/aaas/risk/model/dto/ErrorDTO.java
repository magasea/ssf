package com.shellshellfish.aaas.risk.model.dto;

public class ErrorDTO {
    private int code=0;
    private String message;

    public ErrorDTO(String message) {
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