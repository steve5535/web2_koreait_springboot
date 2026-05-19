package com.study.koreait.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostException extends RuntimeException {
    private HttpStatus statusCode;
    public PostException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
