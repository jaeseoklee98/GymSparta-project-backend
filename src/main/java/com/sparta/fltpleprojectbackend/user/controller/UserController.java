package com.sparta.fltpleprojectbackend.user.controller;

import com.sparta.fltpleprojectbackend.exception.CustomException;
import com.sparta.fltpleprojectbackend.jwtutil.JwtUtil;
import com.sparta.fltpleprojectbackend.security.RefreshTokenService;
import com.sparta.fltpleprojectbackend.user.dto.ResponseMessage;
import com.sparta.fltpleprojectbackend.user.dto.UserSignupRequest;
import com.sparta.fltpleprojectbackend.user.exception.UserException;
import com.sparta.fltpleprojectbackend.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

  private final UserService userService;
  private final JwtUtil jwtUtil;

  @Autowired
  public UserController(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @Autowired
  private RefreshTokenService refreshTokenService;

  /**
   * 회원가입 처리
   * @param request 회원가입 요청 정보
   * @return ResponseEntity<ResponseMessage>
   */
  @PostMapping("/user/signup")
  public ResponseEntity<ResponseMessage> signup(@RequestBody UserSignupRequest request) {
    try {
      userService.signup(request);
      ResponseMessage response = ResponseMessage.success("회원가입 성공");
      return ResponseEntity.ok(response);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(ResponseMessage.error(e.getErrorType().getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ResponseMessage.error("회원가입 실패: " + e.getMessage()));
    }
  }

  /**
   * 회원 탈퇴 처리 (사용자)
   * @param request HTTP 요청
   * @return ResponseEntity<ResponseMessage>
   */
  @DeleteMapping("/profile/users/signout")
  public ResponseEntity<ResponseMessage> deleteUser(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("유효하지 않은 토큰입니다."));
    }

    String token = authHeader.substring(7);
    if (jwtUtil.validateToken(token)) {
      String username = jwtUtil.getUsername(token);
      if (username == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ResponseMessage.error("유효하지 않은 토큰입니다."));
      }

      try {
        userService.deleteUser(username);
        refreshTokenService.deleteByUsername(username);
        SecurityContextHolder.clearContext();
        ResponseMessage response = ResponseMessage.success("회원탈퇴 성공");
        return ResponseEntity.ok(response);
      } catch (UserException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ResponseMessage.error("해당 사용자를 찾을 수 없습니다."));
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseMessage.error("회원탈퇴 실패: " + e.getMessage()));
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("유효하지 않은 토큰입니다."));
    }
  }

  /**
   * 회원 탈퇴 처리 (점주)
   * @param request HTTP 요청
   * @return ResponseEntity<ResponseMessage>
   */
  @DeleteMapping("/owner/profile/signout")
  public ResponseEntity<ResponseMessage> deleteOwner(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("유효하지 않은 토큰입니다."));
    }

    String token = authHeader.substring(7);
    if (jwtUtil.validateToken(token)) {
      String username = jwtUtil.getUsername(token);
      if (username == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ResponseMessage.error("유효하지 않은 토큰입니다."));
      }

      try {
        userService.deleteOwner(username);
        refreshTokenService.deleteByUsername(username);
        SecurityContextHolder.clearContext();
        ResponseMessage response = ResponseMessage.success("점주 탈퇴 성공");
        return ResponseEntity.ok(response);
      } catch (UserException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ResponseMessage.error("해당 사용자를 찾을 수 없습니다."));
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseMessage.error("점주 탈퇴 실패: " + e.getMessage()));
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ResponseMessage.error("유효하지 않은 토큰입니다."));
    }
  }
}