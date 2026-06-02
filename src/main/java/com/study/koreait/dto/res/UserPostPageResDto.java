package com.study.koreait.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPostPageResDto {
    private List<PostWithCommentsResDto> items;
    private int page;
    private int size;
    private long total;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrev;

    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostWithCommentsResDto {
        private int postId;
        private String title;
        private String content;
        private LocalDateTime createAt;
        private List<String> comments;
    }
}
