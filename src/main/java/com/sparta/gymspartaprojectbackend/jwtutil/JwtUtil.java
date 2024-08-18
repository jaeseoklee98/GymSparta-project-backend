package com.sparta.gymspartaprojectbackend.jwtutil;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

  private final long accessTokenValidity = 1800000; // (유효기간) 30분
  private final long refreshTokenValidity = 3600000; // (유효기간) 1시간

  /**
   * 사용자 이름으로 액세스 토큰을 생성
   *
   * @param username 사용자 이름
   * @return 생성된 액세스 토큰
   */
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

  /**
   * 사용자 이름으로 리프레시 토큰을 생성
   *
   * @param username 사용자 이름
   * @return 생성된 리프레시 토큰
   */
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

  /**
   * 토큰에서 사용자 이름을 추출
   *
   * @param token JWT 토큰
   * @return 토큰에서 추출한 사용자 이름, 유효하지 않은 경우 null
   */
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

  /**
   * 토큰의 유효성을 검증
   *
   * @param token JWT 토큰
   * @return 토큰이 유효하면 true, 그렇지 않으면 false
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new CustomException(ErrorType.TOKEN_EXPIRED);
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public long getRefreshTokenValidity() {
    return refreshTokenValidity;
  }

  /**
   * 리프레시 토큰의 유효 기간을 반환
   *
   * @return 리프레시 토큰 유효 기간 (밀리초 단위)
   */
  public String refreshToken(String refreshToken) {
    if (validateToken(refreshToken)) {
      String username = getUsername(refreshToken);
      return generateAccessToken(username);
    } else {
      throw new CustomException(ErrorType.INVALID_TOKEN);
    }
  }
}