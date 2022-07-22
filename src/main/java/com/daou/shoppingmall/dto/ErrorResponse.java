package com.daou.shoppingmall.dto;

import lombok.Getter;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private String code;
    private int status;
    private List<CustomFieldError> fieldErrors;

    private ErrorResponse(Builder builder) {
        this.timestamp = builder.timestamp;
        this.message = builder.message;
        this.code = builder.code;
        this.status = builder.status;
        this.fieldErrors = builder.fieldErrors;
    }

    public static class Builder {
        private LocalDateTime timestamp = LocalDateTime.now();
        private String message;
        private String code;
        private int status;
        private List<CustomFieldError> fieldErrors;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder fieldErrors(Errors errors) {
            this.fieldErrors = errors.getFieldErrors().stream().map(error->
                 CustomFieldError.builder().code(error.getCode())
                        .status(error.getRejectedValue())
                        .message(error.getDefaultMessage())
                        .build()
            ).collect(Collectors.toList());
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
