package com.im.apigateway.exception;

public class UnAuthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnAuthorizedException(String msg) {
        super(msg);
    }
}
