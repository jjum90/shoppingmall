package com.daou.shoppingmall.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private String code;
    private int status;

    public CustomException(String code, int status, String message) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public CustomException(String code, int status, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = status;
    }

    public CustomException(String code, int status,Throwable cause) {
        super(cause);
        this.code = code;
        this.status = status;
    }
}
