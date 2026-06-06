package com.study.koreait.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Users {
    private String userId;
    private String username;
    private String password;
    private String name;
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private Roles roles; // fk대신 객체 가짐 1:1 매핑
}
