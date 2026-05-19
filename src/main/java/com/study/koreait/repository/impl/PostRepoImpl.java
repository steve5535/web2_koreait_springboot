package com.study.koreait.repository.impl;

import com.study.koreait.entity.Post;
import com.study.koreait.exception.PostException;
import com.study.koreait.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class PostRepoImpl implements PostRepository {

    // DB대응
    private final List<Post> posts = new ArrayList<>(
            Arrays.asList(
                    Post.builder().postId(1).title("첫번째 게시물").content("1빠").build(),
                    Post.builder().postId(2).title("페이커 vs 손흥민").content("난 페이커").build(),
                    Post.builder().postId(3).title("스프링브트 공부").content("너무 어려워요").build()
            )
    );
    // 컨트롤러까지 코드를 완성시켜 주세요.
    @Override
    public List<Post> findAllPosts() {
        return posts;
    }

    @Override
    public Post findPostById(int id) {
        Optional<Post> optPost = posts.stream()
                .filter(p -> p.getPostId() == id)
                .findFirst();

        if (optPost.isEmpty()) {
            throw new PostException("해당 id는 존재하지 않습니다", HttpStatus.NOT_FOUND);
        }
        return optPost.get();
    }

    // 실습) Post도 controller까지 작성을 완료해 주세요
    // dto가 없다면, 생성을 하여 완성시켜 주세요
    @Override
    public int insertPost(Post post) {
        int maxId = 0;
        for (Post p : posts) {
            int postId = p.getPostId();
            if (postId > maxId) {
                maxId = postId;
            }
        }

        Post newPost = Post.builder()
                .postId(maxId + 1)
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        posts.add(newPost);

        return 1;
    }

    @Override
    public int deletePostById(int id) {
        Optional<Post> optPost = posts.stream()
                .filter(p -> p.getPostId() == id)
                .findFirst();

        Post post = optPost.orElseThrow(() -> new PostException("해당 게시글은 존재하지 않습니다", HttpStatus.NOT_FOUND));

        posts.remove(post);
        log.info("게시글 삭제 완료: {}", post);

        return 1;
    }

}
