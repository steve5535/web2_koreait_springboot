package com.study.koreait.mapper;

import com.study.koreait.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// 자바버전 mapper
// PostMapper를 implements한 실제 클래스는
// mybatis 라이브러리가 서버시작할때 xml을 보고 알아서 bean등록을 함
@Mapper
public interface PostMapper {
    List<Post> findAll();
    List<Post> findAllPostsWithComments();
}
