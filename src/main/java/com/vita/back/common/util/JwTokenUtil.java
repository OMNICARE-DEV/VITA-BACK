package com.vita.back.common.util;

import com.vita.back.common.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwTokenUtil {
    private final JwtConfig jwtConfig;

    public JwTokenUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }
    /* JWT 생성*/
    public String generateToken(String commonUserNo) {
        long now = System.currentTimeMillis();
        try {
            String token = Jwts.builder()
                    .setSubject(commonUserNo)
                    .claim("type", "access")
                    .setIssuedAt(new Date(now))
                    .setExpiration(new Date(now + jwtConfig.getRefreshExpiration() * 1000)) // 365일 1년
                    .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                    .compact();
            log.debug("Generated Token: {}", token);
            return token;
        } catch (Exception e) {
            log.error("Token generation failed", e);
            throw new RuntimeException("Token generation failed", e);
        }
    }

    /* refreshToken 생성*/
    public String generateRefreshToken(String commonUserNo) {
        long now = System.currentTimeMillis();
        try {
            String refreshToken = Jwts.builder()
                    .setSubject(commonUserNo)
                    .claim("type", "refresh")
                    .setIssuedAt(new Date(now))
                    .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000)) // 365일 1년
                    .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                    .compact();
            log.debug("Generated Refresh Token: {}", refreshToken);
            return refreshToken;
        } catch (Exception e) {
            log.error("refreshToken generation failed", e);
            throw new RuntimeException("refreshToken generation failed", e);
        }
    }

    /* JWT에서 클레임 추출 */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody();
    }

    /* JWT에서 통합회원번호 추출 */
    public String getCommonUserNoFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /* JWT 만료 확인*/
    private boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    /* JWT 검증*/
    public boolean validateToken(String token, String TokenKey) {
        try {
            final String commonUserNo = getCommonUserNoFromToken(token);
            return (commonUserNo.equals(TokenKey) && !isTokenExpired(token));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
