package com.sparta.fltpleprojectbackend.jwtutil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secretKey;

  private final long accessTokenValidity = 1800000; // 30분
  private final long refreshTokenValidity = 3600000; // 1시간

  // JWT 액세스 토큰 생성
  public String generateAccessToken(String username) {
    Claims claims = Jwts.claims().setSubject(username);
    Date now = new Date();
    Date validity = new Date(now.getTime() + accessTokenValidity);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  // JWT 리프레시 토큰 생성
  public String generateRefreshToken(String username) {
    Claims claims = Jwts.claims().setSubject(username);
    Date now = new Date();
    Date validity = new Date(now.getTime() + refreshTokenValidity);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  // 토큰에서 유저 이름 추출
  public String getUsername(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    } catch (JwtException | IllegalArgumentException e) {
      System.out.println("Invalid JWT token: " + e.getMessage());
      return null;
    }
  }

  // 토큰 유효성 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  // 리프레시 토큰 유효기간 반환
  public long getRefreshTokenValidity() {
    return refreshTokenValidity;
  }
}