package com.shellshellfish.aaas.userinfo.aop;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(final String message) {
        super(message);
    }
}
