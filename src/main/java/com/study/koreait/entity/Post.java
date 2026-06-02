package com.study.koreait.entity;

import com.study.koreait.dto.res.FindPostResDto;
import com.study.koreait.dto.res.UserPostPageResDto.PostWithCommentsResDto;
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
    private String userId;

    // comments는 post_id를 fk로 들고있음
    // Post:Comments 는 1:n관계
    // 그래프? post.getComments()
    private List<Comments> comments;

    public FindPostResDto toFindPostResDto() {
        return FindPostResDto.builder()
                .title(title)
                .content(content)
                .build();
    }

    public PostWithCommentsResDto toPostWithCommentsResDto() {
        List<String> commentBodies = comments.stream()
                .map(Comments::getBody)
                .toList();
        return PostWithCommentsResDto.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .createAt(createAt)
                .comments(commentBodies)
                .build();
    }
}
