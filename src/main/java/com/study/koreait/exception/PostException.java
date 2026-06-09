package com.study.koreait.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostException extends BusinessException {
    public PostException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
