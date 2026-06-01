package com.study.koreait.mapper;

import com.study.koreait.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 자바버전 mapper
// PostMapper를 implements한 실제 클래스는
// mybatis 라이브러리가 서버시작할때 xml을 보고 알아서 bean등록을 함
@Mapper
public interface PostMapper {
    List<Post> findAll();
    List<Post> findAllPostsWithComments();
    int insertPost(Post post);

    // 매개변수가 2개 이상일경우에 @Param() 작성필요
    int deletePostById(@Param("id") int id);

    // <if> 실습
    // 제목과 내용에 대해서 and조건으로 조립되는 동적 쿼리
    List<Post> detailSearchPosts(
            @Param("titleKeyword") String titleKeyword,
            @Param("contentKeyword") String contentKeyword
    );
    // <foreach> 실습
    // 다중 insert 쿼리
    // 파라미터로 list하나만 넣고 @Param을 안한다
    // collection="list"로 인식시킬 수 있긴 函
    int insertPosts(@Param("posts") List<Post> posts);

    List<Post> findPage(@Param("offset") int offset, @Param("size") int size);
    long countAll();


}
