package com.study.koreait.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StudentException extends RuntimeException {
    private HttpStatus statusCode;

    public StudentException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
