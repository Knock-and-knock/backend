package com.shinhan.knockknock.auth;

import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.exception.MissingTokenException;
import com.shinhan.knockknock.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);
            if (jwtProvider.validateToken(accessToken)) {
                String userNo = jwtProvider.getUserNo(accessToken);
                Authentication authentication = jwtProvider.getAuthentication(userNo);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("🔒 Response Header - Authorization : {}, URI : {}", response.getHeader("Authorization"), request.getRequestURI());
            } else {
                String userNo = jwtProvider.getUserNo(accessToken);
                String refreshToken = "";
                // refresh token 없을 때 예외처리
                try {
                    refreshToken = jwtProvider.getRefreshToken(userNo);
                } catch (Exception e) {
                    log.info("❗ MissingTokenException - {}", e.getMessage());
                }

                if (jwtProvider.validateToken(refreshToken)) {
                    // access token 재발급
                    UserEntity user = userRepository.findById(Long.parseLong(userNo))
                            .orElse(null);
                    if (user == null) {
                        throw new MissingTokenException("회원 정보가 없습니다.");
                    }
                    String newAccessToken = jwtProvider.createAccessToken(user.entityToDto());

                    Authentication authentication = jwtProvider.getAuthentication(userNo);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    response.setHeader("Authorization", newAccessToken);
                    log.info("🔒 Response Header - Authorization : {}, URI : {}", response.getHeader("Authorization"), request.getRequestURI());
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
