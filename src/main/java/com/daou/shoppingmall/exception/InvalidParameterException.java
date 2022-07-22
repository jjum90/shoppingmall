package com.daou.shoppingmall.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class InvalidParameterException extends CustomException{
    private Errors errors;

    public InvalidParameterException(String message, Errors errors) {
        super("40000001", 400, message);
        this.errors = errors;
    }

    public InvalidParameterException(String code, int status, String message, Errors errors) {
        super(code, status, message);
        this.errors = errors;
    }
}
