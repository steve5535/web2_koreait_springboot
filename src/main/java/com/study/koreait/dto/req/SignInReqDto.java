package com.study.koreait.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignInReqDto {
    @NotBlank(message = "아이디를 입력하세요")
    private String username;
    @NotBlank(message = "패스워드를 입력하세요")
    private String password;
}
