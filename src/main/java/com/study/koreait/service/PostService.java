package com.study.koreait.service;

import com.study.koreait.dto.req.AddPostReqDto;
import com.study.koreait.dto.req.PageReqDto;
import com.study.koreait.dto.res.FindPostResDto;
import com.study.koreait.dto.req.SearchPostReqDto;
import com.study.koreait.dto.res.PostPageResDto;
import com.study.koreait.entity.Post;
import com.study.koreait.mapper.PostMapper;
import com.study.koreait.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final PostMapper mapper;

    public List<FindPostResDto> getAllPost() {
        return mapper.findAll()
                .stream()
                .map(p -> new FindPostResDto(p.getTitle(), p.getContent()))
                .toList();
    }

    public FindPostResDto getPostById(int id) {
        Post post = repository.findPostById(id);

        return new FindPostResDto(post.getTitle(), post.getContent());
    }

    public int addPost(AddPostReqDto dto) {
        return mapper.insertPost(dto.toEntity());
    }

    public int removePost(int id) {
        return mapper.deletePostById(id);
    }

    // 추후에 dto로 교체
    public List<Post> getPostsWithComments() {
        return mapper.findAllPostsWithComments();
    }

    public List<FindPostResDto> searchDynamicPosts(SearchPostReqDto dto) {
        return mapper.detailSearchPosts(dto.getTitleKeyword(), dto.getContentKeyword())
                .stream()
                .map(Post::toFindPostResDto)
                .toList();
    }

    public int addBulkPosts(List<AddPostReqDto> dtos) {
        return mapper.insertPosts(dtos.stream().map(AddPostReqDto::toEntity).toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public PostPageResDto getPostPage(PageReqDto dto) {
        int page = Math.max(dto.getPage(), 1);
        int size = dto.getSize();
        if (size < 1) size = 10;
        if (size > 50) size = 50;

        int offset = (page - 1) * size;
        List<FindPostResDto> items =  mapper.findPage(offset, size)
                .stream()
                .map(Post::toFindPostResDto)
                .toList();

        long totalPostCount = mapper.countAll();
        int totalPages = (int) totalPostCount / size;
        if (totalPages % size > 0) totalPages++;

        boolean hasNext = page < totalPages;
        boolean hasPrev = page > 1;

        return new PostPageResDto(items, page, size, totalPostCount, totalPages, hasNext, hasPrev);
    }
}
