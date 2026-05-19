package com.study.koreait.repository;

import com.study.koreait.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    List<Post> findAllPosts();

    Post findPostById(int id);

    int insertPost(Post post);

    int deletePostById(int id);
}
