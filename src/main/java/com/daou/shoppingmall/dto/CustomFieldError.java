package com.daou.shoppingmall.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomFieldError {
    private String code;
    private Object status;
    private String message;
}
