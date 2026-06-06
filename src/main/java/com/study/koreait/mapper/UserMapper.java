package com.study.koreait.mapper;

import com.study.koreait.entity.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    // 회원 등록
    int addUser(Users user);
    // 중복 확인용
    Optional<Users> getUserByUsername(String username);
    Optional<Users> getUserByEmail(String email);

}
