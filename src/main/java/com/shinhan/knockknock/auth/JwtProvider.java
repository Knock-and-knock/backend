package com.shinhan.knockknock.auth;

import com.shinhan.knockknock.domain.dto.user.LoginUserResponse;
import com.shinhan.knockknock.domain.entity.TokenEntity;
import com.shinhan.knockknock.domain.entity.UserEntity;
import com.shinhan.knockknock.exception.MissingTokenException;
import com.shinhan.knockknock.repository.TokenRepository;
import com.shinhan.knockknock.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    private final Key key;
    private final long accessExpirationTime;
    private final long refreshExpirationTime;
    private final TokenRepository tokenRepository;
    private final CustomUserDetailsService userDetailsService;

    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.access-expiration-time}") long accessExpirationTime,
                       @Value("${jwt.refresh-expiration-time}") long refreshExpirationTime,
                       TokenRepository tokenRepository,
                       CustomUserDetailsService userDetailsService) {
        this.tokenRepository = tokenRepository;
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public String createAccessToken(LoginUserResponse user) {
        Claims claims = Jwts.claims();
        claims.put("userNo", user.getUserNo());
        claims.put("userId", user.getUserId());
        claims.put("userName", user.getUserName());
        claims.put("userType", user.getUserType());

        return createToken(user, accessExpirationTime, claims);
    }

    public String createRefreshToken(LoginUserResponse loginUserResponse) {
        Claims claims = Jwts.claims();
        claims.put("userNo", loginUserResponse.getUserNo());
        claims.put("userType", loginUserResponse.getUserType());

        return createToken(loginUserResponse, refreshExpirationTime, claims);
    }

    private String createToken(LoginUserResponse user, Long expirationTime, Claims claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();
    }

    public String getUserNo(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userNo", Long.class)
                    .toString();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우, 클레임을 가져올 수 있음
            Claims claims = e.getClaims();
            return claims.get("userNo", Long.class).toString();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("❗ Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("❗ Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("❗ Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("❗ JWT claims string is empty.");
        }
        return false;
    }

    public String getRefreshToken(String userNo) {
        TokenEntity tokenEntity = tokenRepository.findByUser(UserEntity.builder()
                        .userNo(Long.parseLong(userNo))
                        .build())
                .orElseThrow(() -> new MissingTokenException("Refresh Token이 없습니다."));
        return tokenEntity.getRefreshToken();
    }

    public Authentication getAuthentication(String userNo) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userNo);
        UsernamePasswordAuthenticationToken usernamePasswordToken = null;
        if(userDetails != null) {
            usernamePasswordToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        return usernamePasswordToken;
    }

    public long getUserNoFromHeader(String header) {
        String token = "";
        if (header.startsWith("Bearer ")) {
            token = header.substring(7);
        }
        return Long.parseLong(getUserNo(token));
    }
}
