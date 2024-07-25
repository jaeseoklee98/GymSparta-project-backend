package com.sparta.fltpleprojectbackend.user.controller;

import com.sparta.fltpleprojectbackend.jwtutil.JwtUtil;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fltpleprojectbackend.user.dto.ResponseMessage;
import com.sparta.fltpleprojectbackend.user.dto.UpdatePasswordRequest;
import com.sparta.fltpleprojectbackend.user.dto.UpdateUserProfileRequest;
import com.sparta.fltpleprojectbackend.user.dto.UserSignupRequest;
import com.sparta.fltpleprojectbackend.user.exception.UserException;
import com.sparta.fltpleprojectbackend.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  @PostMapping("/user/signup")
  public ResponseEntity<ResponseMessage> signup(@RequestBody UserSignupRequest request) {
    try {
      userService.signup(request);
      ResponseMessage response = ResponseMessage.success("회원가입 성공");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ResponseMessage.error("회원가입 실패: " + e.getMessage()));
    }
  }

  @DeleteMapping("/profile/users/signout")
  public ResponseEntity<ResponseMessage> deleteUser(HttpServletRequest request) {
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
      userService.deleteUser(username);
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
  }

  /**.
   * 유저 프로필 변경
   *
   * @param userDetails 유저 정보
   * @param userRequest 새 프로필 정보
   * @return statusCode: 200, message: 프로필 변경 완료
   */
  @PutMapping("/profile/user")
  public ResponseEntity<?> updateUserProfile (
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @Valid @RequestBody UpdateUserProfileRequest userRequest) {
    // TODO: 리스폰 형식 컨벤션에 맞추기
    userService.updateUserProfile(userRequest, userDetails);

    return ResponseEntity.ok("프로필 변경 완료");
  }

  /**.
   * 유저 비밀번호 변경
   *
   * @param userDetails 유저 정보
   * @param userRequest 새 비밀번호 정보
   * @return statusCode: 200, message: 변경 완료
   */
  @PutMapping("/profile/users/password")
  public ResponseEntity<?> updateUserPassword (
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @Valid @RequestBody UpdatePasswordRequest userRequest) {
    // TODO: 리스폰 형식 컨벤션에 맞추기
    userService.updateUserPassword(userRequest, userDetails);

    return ResponseEntity.ok("비밀번호 변경 완료");
  }
}