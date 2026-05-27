package com.study.koreait.entity;

import com.study.koreait.dto.FindPostResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
public class Post {
    private int postId;
    private String title;
    private String content;
    private LocalDateTime createAt;

    // comments는 post_id를 fk로 들고있음
    // Post:Comments 는 1:n관계
    // 그래프? post.getComments()
    private List<Comments> comments;
}
