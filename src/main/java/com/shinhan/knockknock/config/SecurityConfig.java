package com.shinhan.knockknock.config;

import com.shinhan.knockknock.auth.JwtAuthenticationFilter;
import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.exception.CustomAccessDeniedHandler;
import com.shinhan.knockknock.exception.CustomAuthenticationEntryPoint;
import com.shinhan.knockknock.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity  // Spring Security context 설정임을 명시
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private static final String[] WHITE_LIST = {
            "/api/v1/auth/login/**", "/api/v1/users/signup", "/api/v1/users/validation/**",
            "/swagger-ui/**", "/v3/api-docs/**", "/error",
            "/conversation", "/conversation.html", "/stt", "/stt.html",
            "/api/v1/notification/**", "/Notification.html", "/login.html", "/card-history.html"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable); // csrf 토큰 사용 비활성화
        http.cors(Customizer.withDefaults());

        // jwt 기반 인증은 세션 사용하지 않으므로 세션 관리 상태 없음으로 설정
        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 권한 및 인증 예외 핸들러 추가
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 실패 시 처리
                .accessDeniedHandler(new CustomAccessDeniedHandler()) // 권한 부족 시 처리
        );

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(WHITE_LIST)
                .permitAll()
                .anyRequest().authenticated());

        http.formLogin(AbstractHttpConfigurer::disable);    // spring 제공 form login 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable);    // HTTP 기본 인증 비활성화

        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider, userRepository) {
            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
                return Arrays.stream(WHITE_LIST).anyMatch(path ->
                        new AntPathMatcher().match(path, request.getServletPath()));
            }
        }, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
