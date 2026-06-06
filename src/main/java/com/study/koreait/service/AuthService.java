package com.study.koreait.service;

import com.study.koreait.dto.req.SignUpReqDto;
import com.study.koreait.entity.Users;
import com.study.koreait.exception.UserException;
import com.study.koreait.jwt.JwtUtil;
import com.study.koreait.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    @Transactional(rollbackFor = Exception.class)
    public void signUp(SignUpReqDto dto) {
        // 아이디 중복검사 - Optional 안에 객체가 null이 아니라면
        if (userMapper.getUserByUsername(dto.getUsername()).isPresent()) {
            throw new UserException("이미 존재하는 아이디입니다", HttpStatus.CONFLICT); // 409
        }

        if (userMapper.getUserByEmail(dto.getEmail()).isPresent()) {
            throw new UserException("이미 존재하는 이메일입니다", HttpStatus.CONFLICT); // 409
        }

        Users user = dto.toEntity();
        user.setUserId(UUID.randomUUID().toString());
        // "1q2w3e4r!" -> 해쉬 -> 난수
        user.setPassword(encoder.encode(dto.getPassword()));

        int sc = userMapper.addUser(user);
        if (sc <= 0) {
            throw new UserException("회원가입 중 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
