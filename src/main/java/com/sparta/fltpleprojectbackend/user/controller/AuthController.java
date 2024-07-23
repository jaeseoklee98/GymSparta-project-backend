package com.sparta.fltpleprojectbackend.user.controller;

import com.sparta.fltpleprojectbackend.user.dto.LoginRequest;
import com.sparta.fltpleprojectbackend.user.dto.ResponseMessage;
import com.sparta.fltpleprojectbackend.jwtutil.JwtUtil;
import com.sparta.fltpleprojectbackend.security.RefreshTokenService;
import com.sparta.fltpleprojectbackend.security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  private RefreshTokenService refreshTokenService;

  /**
   * 로그인 처리
   * @param loginRequest 로그인 요청 정보 (아이디, 비밀번호)
   * @return ResponseEntity<ResponseMessage<Map<String, String>>> JWT 액세스 토큰과 리프레시 토큰
   */
  @PostMapping("/login")
  public ResponseEntity<ResponseMessage<Map<String, String>>> login(@RequestBody LoginRequest loginRequest) {
    // 사용자 인증
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
    );

    // 인증된 사용자 정보 로드
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    // JWT 토큰 생성
    String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());

    // 리프레시 토큰 생성 및 저장
    String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

    // 토큰을 Map에 담아 반환
    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", accessToken);
    tokens.put("refreshToken", refreshToken);

    // 응답 메시지 생성
    ResponseMessage<Map<String, String>> response = ResponseMessage.success("로그인 성공", tokens);
    return ResponseEntity.ok(response);
  }

  /**
   * 로그아웃 처리
   * @param request HTTP 요청
   * @return ResponseEntity<ResponseMessage<String>> 로그아웃 성공 메시지
   */
  @PostMapping("/logout")
  public ResponseEntity<ResponseMessage<String>> logout(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      String username = jwtUtil.getUsername(token);
      refreshTokenService.deleteByUsername(username);
    }

    ResponseMessage<String> response = ResponseMessage.success("로그아웃 성공", null);
    return ResponseEntity.ok(response);
  }
}