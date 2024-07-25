package com.sparta.fltpleprojectbackend.owner.controller;

import com.sparta.fltpleprojectbackend.jwtutil.JwtUtil;
import com.sparta.fltpleprojectbackend.owner.dto.OwnerSignupRequest;
import com.sparta.fltpleprojectbackend.owner.service.OwnerService;
import com.sparta.fltpleprojectbackend.user.dto.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OwnerController {

  private final OwnerService ownerService;
  private final JwtUtil jwtUtil;

  @Autowired
  public OwnerController(OwnerService ownerService, JwtUtil jwtUtil) {
    this.ownerService = ownerService;
    this.jwtUtil = jwtUtil;
  }

  // 점주 회원가입 엔드포인트
  @PostMapping("/owners/signup")
  public ResponseEntity<ResponseMessage> signup(@RequestBody OwnerSignupRequest request) {
    try {
      ownerService.signup(request);
      ResponseMessage response = ResponseMessage.success("회원가입 성공");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ResponseMessage.error("회원가입 실패: " + e.getMessage()));
    }
  }

  // 점주 회원탈퇴 엔드포인트
  @DeleteMapping("/profile/owners/signout")
  public ResponseEntity<ResponseMessage> deleteOwner(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("유효하지 않은 토큰입니다."));
    }

    String token = authHeader.substring(7);
    if (!jwtUtil.validateToken(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("유효하지 않은 토큰입니다."));
    }

    String username = jwtUtil.getUsername(token);
    if (username == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("유효하지 않은 토큰입니다."));
    }

    try {
      ownerService.deleteOwner(username);
      SecurityContextHolder.clearContext();
      ResponseMessage response = ResponseMessage.success("회원탈퇴 성공");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ResponseMessage.error("회원탈퇴 실패: " + e.getMessage()));
    }
  }
}