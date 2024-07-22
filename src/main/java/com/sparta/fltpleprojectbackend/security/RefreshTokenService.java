package com.sparta.fltpleprojectbackend.security;

import com.sparta.fltpleprojectbackend.jwtutil.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtUtil jwtUtil;

  @Autowired
  public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.jwtUtil = jwtUtil;
  }

  /**
   * 리프레시 토큰 생성 및 저장
   * @param username 사용자 이름
   * @return 저장된 리프레시 토큰
   */
  public RefreshToken createRefreshToken(String username) {
    String token = jwtUtil.generateRefreshToken(username);
    RefreshToken refreshToken = RefreshToken.builder()
        .token(token)
        .username(username)
        .expiryDate(jwtUtil.getExpiryDate(token))
        .build();
    return refreshTokenRepository.save(refreshToken);
  }

  /**
   * 리프레시 토큰 삭제
   * @param token 삭제할 리프레시 토큰
   */
  public void deleteByToken(String token) {
    refreshTokenRepository.deleteByToken(token);
  }

  /**
   * 리프레시 토큰 검증
   * @param token 검증할 리프레시 토큰
   * @return 유효한 토큰인지 여부
   */
  public boolean isRefreshTokenValid(String token) {
    Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
    return refreshToken.isPresent() && jwtUtil.validateToken(token);
  }

  /**
   * 사용자와 관련된 모든 리프레시 토큰 삭제
   * @param username 사용자 이름
   */
  public void deleteByUsername(String username) {
    refreshTokenRepository.deleteByUsername(username);
  }
}