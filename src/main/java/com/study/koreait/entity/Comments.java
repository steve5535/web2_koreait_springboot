package com.study.koreait.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comments {
    private int commentId;
    private int postId;
    private String author;
    private String body;

    /*
        데이터베이스는 외래키를 통해 테이블끼리 결합
        자바에서는 참조를 통해 객체끼리 결합(필드 선언)

        1:n 관계 - 외래키를 가진쪽이 n
        1:1 관계 - 양방향 참조만 조심

        m:n 관계 - 중간테이블을 만들어서 m:1 <> 1:n
     */
}
