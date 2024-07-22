package com.sparta.fltpleprojectbackend.jwtutil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secretKey;

  private final long ACCESS_TOKEN_VALIDITY = 1800000; // 30분
  private final long REFRESH_TOKEN_VALIDITY = 3600000; // 1시간

  /**
   * JWT 액세스 토큰 생성
   * @param username 유저 이름
   * @return 생성된 JWT 액세스 토큰
   */
  public String generateAccessToken(String username) {
    Claims claims = Jwts.claims().setSubject(username);
    Date now = new Date();
    Date validity = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  /**
   * JWT 리프레시 토큰 생성
   * @param username 유저 이름
   * @return 생성된 JWT 리프레시 토큰
   */
  public String generateRefreshToken(String username) {
    Claims claims = Jwts.claims().setSubject(username);
    Date now = new Date();
    Date validity = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  /**
   * 토큰에서 유저 이름 추출
   * @param token JWT 토큰
   * @return 유저 이름
   */
  public String getUsername(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  /**
   * 토큰 유효성 검증
   * @param token JWT 토큰
   * @return 유효한 토큰인지 여부
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 토큰 만료일자 가져오기
   * @param token JWT 토큰
   * @return 토큰 만료일자
   */
  public LocalDateTime getExpiryDate(String token) {
    Date expiration = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration();
    return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}