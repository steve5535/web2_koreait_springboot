package com.study.koreait.dto.req;

import com.study.koreait.entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpReqDto {
    @NotBlank(message = "아이디를 입력하세요")
    private String username;

    @NotBlank(message = "패스워드를 입력하세요")
    // 영문자, 숫자, 특수 문자를 하나씩 포함 8~16자
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    private String password;

    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @Email(message = "이메일 형식이 아닙니다")
    @NotBlank(message = "이메일를 입력하세요")
    private String email;

    public Users toEntity() {
        return Users.builder()
                .username(username)
                .name(name)
                .email(email)
                .build();
    }
}
