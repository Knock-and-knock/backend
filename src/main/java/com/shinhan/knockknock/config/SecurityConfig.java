package com.shinhan.knockknock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private static final String[] WHITE_LIST = {"/api/v1/**", "/swagger-ui/**", "/v3/api-docs/**", "/error"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable); // csrf 토큰 사용 비활성화
        http
            .authorizeHttpRequests((auth)->auth
                .requestMatchers(WHITE_LIST)
                .permitAll()
                .anyRequest().authenticated());

        http.formLogin(AbstractHttpConfigurer::disable);    // spring 제공 form login 비활성화

        http.addFilter(new UsernamePasswordAuthenticationFilter());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
