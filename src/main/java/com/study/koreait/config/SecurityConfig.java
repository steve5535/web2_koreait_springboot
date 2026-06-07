package com.study.koreait.config;

import com.study.koreait.jwt.JwtAuthenticationEntryPoint;
import com.study.koreait.jwt.JwtAuthenticationFilter;
import com.study.koreait.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig { // 시큐리티 관련 설정만

    // 필터를 빈으로 주입하지않고, 직접 new로 생성한다
    // Filter로 끝나는 빈이 등록되면, 자동으로 서블릿 필터를 등록함(체인에 등록하는게 x)
    private final JwtUtil jwtUtil;

    // 회원가입시 비밀번호는 평문으로 저장하면 안됨
    // 암호화를 해서 db에 저장해야함.
    // 단방향 해시만 법으로 허용됨

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    // cors 에러
    // 브라우저는 다른 origin(도메인+포트 조합)으로의 요청을 기본 차단
    // 프론트(localhost:5173)에서 이서버(localhost:8080)으로 호출할때
    // 막히지 않도록 허용규칙을 등록
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern(CorsConfiguration.ALL);
        cors.addAllowedHeader(CorsConfiguration.ALL);
        cors.addAllowedMethod(CorsConfiguration.ALL);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors); // 모든 url패턴에 대해 적용
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 위에서 만든 cors를 필터체인에 적용
        http.cors(Customizer.withDefaults());

        // 세션기반 기능들을 off
        http.csrf(csrf -> csrf.disable());
        http.formLogin(form -> form.disable()); // ssr 사용안함
        http.httpBasic(basic -> basic.disable());
        http.logout(logout -> logout.disable());
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 안만들것

        // 인가 규칙(중요-커스텀 가능)
        http.authorizeHttpRequests(auth -> {
            // requestMatchers
            /*
                localhost:8080/post/** -> post/ 하위 모든 엔드포인트 허가
                localhost:8080/post/* -> 단일만 허용 /post/users/11 (x)
             */
            // 인가방식은 우리가 직접 커스텀한 jwt방식을 사용할 것
            auth.requestMatchers("/post/**", "/product/**")
                    .authenticated()
                    .anyRequest().permitAll();
        });

        // 끼워넣기
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 필터단 예외 핸들러 등록
        http.exceptionHandling(eHandler -> eHandler.authenticationEntryPoint(jwtAuthenticationEntryPoint()));

        // oauth2 소셜 로그인 설정
        http.oauth2Login(Customizer.withDefaults());

        return http.build();
    }

}
