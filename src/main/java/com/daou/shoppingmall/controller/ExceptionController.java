package com.daou.shoppingmall.controller;

import com.daou.shoppingmall.dto.ErrorResponse;
import com.daou.shoppingmall.exception.InvalidParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@Slf4j
@ControllerAdvice
public class ExceptionController {
    /**
     * Input value Exception
     * @param exception
     * @return
     */
    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<ErrorResponse> handlerInvalidParameterException(InvalidParameterException exception) {
        com.daou.shoppingmall.dto.ErrorResponse response = new ErrorResponse.Builder()
                .code(exception.getCode())
                .status(exception.getStatus())
                .message(exception.getMessage())
                .fieldErrors(exception.getErrors())
                .build();
        return new ResponseEntity<>(response, HttpStatus.resolve(exception.getStatus()));
    }
}
