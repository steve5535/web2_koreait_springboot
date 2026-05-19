package com.study.koreait.dto;

import com.study.koreait.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// StudyController2에 AddPostReqDot를 받는 postmapping 메서드르 작성
// 받은 데이터를 로그에 남겨주세요. 201을 클라이언트로 리턴
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddPostReqDto {
    // Validation 라이브러리
    @NotBlank(message = "제목은 비울 수 없습니다")
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다")
    private String title;

    @NotBlank(message = "내용은 비울 수 없습니다")
    @Size(max = 5000, message = "내용은 5000자를 초과할 수 없습니다")
    private String content;

    /*
        1. 문자열
            @NotBlank - null, "", "  " 허용 x
            @NotEmpty - null, "" 허용 x
            @Size(min=, max=) 길이제한
            @Email // 이메일 형식검사
            @Pattern(regexp="") 정규식검사
        2. 숫자
            @Min, @Max
            @Positive @Negative
        3. 객체
            @Valid // 내부 객체를 검사
            @NotNull // null만 검사
        4. 날짜
            @Future // 미래날짜만
            @Past // 과거날짜만

     */

    // dto가 자동으로 생성될텐데,
    // 우리가 원하는 값이 아니면, 예외(Exception)을 일으킴.

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

}
