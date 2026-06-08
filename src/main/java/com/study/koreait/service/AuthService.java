package com.study.koreait.service;

import com.study.koreait.dto.req.SignInReqDto;
import com.study.koreait.dto.req.SignUpReqDto;
import com.study.koreait.dto.res.SignInResDto;
import com.study.koreait.entity.OAuth2User;
import com.study.koreait.entity.Roles;
import com.study.koreait.entity.Users;
import com.study.koreait.exception.UserException;
import com.study.koreait.jwt.JwtUtil;
import com.study.koreait.mapper.OAuth2UserMapper;
import com.study.koreait.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final OAuth2UserMapper oAuth2UserMapper;
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

    public SignInResDto signIn(SignInReqDto dto) {
        // 1) 아이디로 회원조회
        Users user = userMapper.getUserByUsername(dto.getUsername())
                .orElseThrow(() -> new UserException("사용자 정보를 잘못 입력하셨습니다", HttpStatus.BAD_REQUEST));

        // 2) 비밀번호 비교
        // matches(평문, 저장된 해시) -> 평문으로 해싱해 비교해줌
        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UserException("사용자 정보를 잘못 입력하셨습니다", HttpStatus.BAD_REQUEST);
        }

        // 토큰발급
        return issueAccessToken(user);
    }

    /*
        1) 이전에 소셜로 로그인한적 있다 -> 토큰만 발급
        2) 이미 gmail로 가입한적있다 -> 연결 -> 토큰 발급
        3) 둘다 없으면 -> 신규 회원가입 -> 소셜 연결 -> 토큰 발급
     */
    @Transactional(rollbackFor = Exception.class)
    public SignInResDto oAuth2SignIn(String provider, String providerUserId, String email) {
        if (provider == null || provider.isBlank() || providerUserId == null || providerUserId.isBlank()) {
            throw new UserException("필수정보가 누락되어 로그인에 실패 하였습니다", HttpStatus.BAD_REQUEST);
        }
        if (email == null || email.isBlank()) {
            throw new UserException(provider + "이메일이 누락되어 로그인에 실패 하였습니다", HttpStatus.BAD_REQUEST);
        }

        // 1) 이미 소셜로그인 한적 있는 경우
        OAuth2User linked = oAuth2UserMapper.findOAuth2UserByProviderInfo(provider, providerUserId).orElse(null);
        if (linked != null) { // 있다면
            Users user = userMapper.getUserById(linked.getUserId())
                    .orElseThrow(() -> new UserException("연결된 회원정보가 없습니다", HttpStatus.BAD_REQUEST));

            return issueAccessToken(user);
        }

        // 2) 소셜 로그인 한적 없고, 해당 email로 일반 회원가입한 이력이 있는경우
        // 소셜만 연결 -> Oauth2User에 userId만 연결
        Users existing = userMapper.getUserByEmail(email).orElse(null);
        if (existing != null) { // 일반회원 가입은 되어있다면
            String userId = existing.getUserId();
            OAuth2User insertData = OAuth2User.builder()
                    .userId(userId)
                    .provider(provider)
                    .providerUserId(providerUserId)
                    .build();
            oAuth2UserMapper.insertOAuth2User(insertData);

            return issueAccessToken(existing);
        }

        // 3) 신규 자동가입
        // OAuth 전용계정을 만들어 줄것 -> 일반 로그인은 안하는 계정
        String userId = UUID.randomUUID().toString();
        Users user = Users.builder()
                .userId(userId)
                // google_asdf12341234
                .username(provider + "_" + providerUserId)
                // 일반로그인 안함 -> 랜덤 password
                .password(encoder.encode(UUID.randomUUID().toString()))
                .name(provider + " 사용자")
                .email(email)
                .build();
        // Users에 user insert
        if (userMapper.addUser(user) <= 0) {
            throw new UserException("OAuth2 가입 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // OauthUser에 OauthUser insert
        OAuth2User insertData = OAuth2User.builder()
                .userId(user.getUserId())
                .provider(provider)
                .providerUserId(providerUserId)
                .build();
        oAuth2UserMapper.insertOAuth2User(insertData);

        // user에 role이 있는걸로 select
        // addUser는 role_id = 1만 넣고 Roles 객체는 안 채우므로, 다시 얽어와야함
        // roleName이 jwt에 들어가야하기 때문
        Users created = userMapper.getUserById(userId)
                .orElseThrow(() -> new UserException("OAuth2 가입 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR));
        // user로 토큰만들어서 return
        return issueAccessToken(created);

    }

    private SignInResDto issueAccessToken(Users user) {
        String subject = user.getUserId(); // subject: 사용자식별데이터
        String roleName = user.getRoles().getRoleName();
        // claims: 추가로 토큰에 심을 key-value 정보
        Map<String, Object> claims = Map.of("role", roleName);
        String accessToken = jwtUtil.generateAccessToken(subject, claims);
        return SignInResDto.builder()
                .accessToken(accessToken)
                .build();
    }


}
