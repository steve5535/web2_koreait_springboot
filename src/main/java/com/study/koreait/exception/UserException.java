package com.study.koreait.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
@Getter
public class UserException extends BusinessException {
    public UserException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
