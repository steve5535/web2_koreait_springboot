package com.study.koreait.service;

import com.study.koreait.dto.AddPostReqDto;
import com.study.koreait.dto.FindPostResDto;
import com.study.koreait.entity.Post;
import com.study.koreait.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;

    public List<FindPostResDto> getAllPost() {
        return repository.findAllPosts()
                .stream()
                .map(p -> new FindPostResDto(p.getTitle(), p.getContent()))
                .toList();
    }

    public FindPostResDto getPostById(int id) {
        Post post = repository.findPostById(id);

        return new FindPostResDto(post.getTitle(), post.getContent());
    }

    public int addPost(AddPostReqDto dto) {
        return repository.insertPost(dto.toEntity());
    }

    public int removePost(int id) {
        return repository.deletePostById(id);
    }
}
