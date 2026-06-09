package com.study.koreait.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductException extends BusinessException {
    public ProductException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
