package com.study.koreait.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 롬복 - 어노테이션으로 보일러플레이트코드를
// 컴파일할때 생성해주는 라이브러리
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentResDto {
    private int id;
    private String name;
    private int age;
}
