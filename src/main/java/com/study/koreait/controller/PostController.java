package com.study.koreait.controller;

import com.study.koreait.dto.req.AddPostReqDto;
import com.study.koreait.dto.req.PageReqDto;
import com.study.koreait.dto.req.PeriodPageReqDto;
import com.study.koreait.dto.req.SearchPostReqDto;
import com.study.koreait.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPost() {
        return ResponseEntity.ok(service.getAllPost());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id) {
        return ResponseEntity.ok(service.getPostById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPost(@RequestBody @Valid AddPostReqDto dto) {
        int successCount = service.addPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(successCount + "건 추가완료");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        int successCount = service.removePost(id);
        return ResponseEntity.ok(successCount + "건 삭제 완료");
    }

    @GetMapping("/all/comments")
    public ResponseEntity<?> getPostsWithComments() {
        return ResponseEntity.ok(service.getPostsWithComments());
    }

    // 원래는 쿼리스트링을 객체로 받으려면, @ModerlAttribute
    // 하지만, json key와 필드변수명이 동일하면 생략 ok
    @GetMapping("/search/dynamic")
    public ResponseEntity<?> searchDynamicPosts(SearchPostReqDto dto) {
        return ResponseEntity.ok(service.searchDynamicPosts(dto));
    }

    @PostMapping("/add/bulk")
    public ResponseEntity<?> addPosts(@RequestBody List<AddPostReqDto> dtos) {
        int sc = service.addBulkPosts(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(sc + "건 추가완료");
    }

    @GetMapping("/page")
    public ResponseEntity<?> getPostPage(PageReqDto dto) {
        return ResponseEntity.ok(service.getPostPage(dto));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUserPostPage(@AuthenticationPrincipal String userId, PeriodPageReqDto dto) {
        return ResponseEntity.ok(service.getUserPostPage(userId, dto));
    }

}
