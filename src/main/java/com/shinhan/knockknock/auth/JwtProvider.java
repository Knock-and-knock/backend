package com.shinhan.knockknock.auth;

import com.shinhan.knockknock.domain.dto.LoginUserResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;
    private final long accessExpirationTime;

    public JwtProvider(@Value("${jwt.secret}") String secretKey, @Value("${jwt.access-expiration-time}") long accessExpirationTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessExpirationTime = accessExpirationTime;
    }

    public String createAccessToken(LoginUserResponse user){
        return createToken(user, accessExpirationTime);
    }

    private String createToken(LoginUserResponse user, Long expirationTime){
        Claims claims = Jwts.claims();
        claims.put("userNo", user.getUserNo());
        claims.put("userId", user.getUserId());
        claims.put("userName", user.getUserName());
        claims.put("userType", user.getUserType());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant())) // set Expire Time
                .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();
    }

    public String getUserNo(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("userNo", Long.class).toString();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }
}
