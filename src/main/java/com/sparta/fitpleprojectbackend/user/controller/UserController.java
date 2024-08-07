package com.sparta.fitpleprojectbackend.user.controller;

import com.sparta.fitpleprojectbackend.common.CommonResponse;
import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.jwtutil.JwtUtil;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.user.dto.UpdatePasswordRequest;
import com.sparta.fitpleprojectbackend.user.dto.UpdateUserProfileRequest;
import com.sparta.fitpleprojectbackend.user.dto.UserSignupRequest;
import com.sparta.fitpleprojectbackend.user.dto.ReadUserResponse;
import com.sparta.fitpleprojectbackend.user.exception.UserException;
import com.sparta.fitpleprojectbackend.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

  public UserController(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  /**
   * 사용자 회원가입
   *
   * @param request 회원가입에 필요한 데이터가 담긴 요청 본문
   * @return 회원가입 성공 또는 실패 메시지를 담은 응답
   */
  @PostMapping("/user/signup")
  public ResponseEntity<CommonResponse<String>> signup(@RequestBody @Valid UserSignupRequest request) {
    try {
      userService.signup(request);
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.OK.value(), "회원가입 성공", "회원가입이 완료되었습니다.");
      return ResponseEntity.ok(response);
    } catch (CustomException e) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.BAD_REQUEST.value(), "회원가입 실패", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    } catch (Exception e) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.BAD_REQUEST.value(), "회원가입 실패", "알 수 없는 오류가 발생했습니다.");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
  }

  /**
   * 사용자 탈퇴
   *
   * @param request HTTP 요청 객체 (Authorization 헤더에서 토큰 추출)
   * @return 탈퇴 성공 또는 실패 메시지를 담은 응답
   */
  @DeleteMapping("/profile/users/signout")
  public ResponseEntity<CommonResponse<String>> deleteUser(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다.", "유효하지 않은 토큰입니다.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    String token = authHeader.substring(7);
    if (!jwtUtil.validateToken(token)) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다.", "유효하지 않은 토큰입니다.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    String username = jwtUtil.getUsername(token);
    if (username == null) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다.", "유효하지 않은 토큰입니다.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    try {
      userService.deleteUser(username);
      SecurityContextHolder.clearContext();
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.OK.value(), "회원탈퇴 성공", "회원탈퇴가 완료되었습니다.");
      return ResponseEntity.ok(response);
    } catch (UserException e) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.NOT_FOUND.value(), "해당 사용자를 찾을 수 없습니다.", "해당 사용자를 찾을 수 없습니다.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } catch (Exception e) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.BAD_REQUEST.value(), "회원탈퇴 실패", "회원탈퇴 실패: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
  }

  /**
   * 유저 프로필 변경
   *
   * @param userDetails 유저 정보
   * @param userRequest 새 프로필 정보
   * @return statusCode: 200, message: 프로필 변경 완료
   */
  @PutMapping("/profile/user")
  public ResponseEntity<CommonResponse<String>> updateUserProfile(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody UpdateUserProfileRequest userRequest) {

    userService.updateUserProfile(userRequest, userDetails);
    CommonResponse<String> response = new CommonResponse<>(
        HttpStatus.OK.value(), "프로필 변경 완료", "프로필이 성공적으로 변경되었습니다.");
    return ResponseEntity.ok(response);
  }

  /**
   * 유저 비밀번호 변경
   *
   * @param userDetails 유저 정보
   * @param userRequest 새 비밀번호 정보
   * @return statusCode: 200, message: 변경 완료
   */
  @PutMapping("/profile/users/password")
  public ResponseEntity<CommonResponse<String>> updateUserPassword(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody UpdatePasswordRequest userRequest) {

    userService.updateUserPassword(userRequest, userDetails);
    CommonResponse<String> response = new CommonResponse<>(
        HttpStatus.OK.value(), "비밀번호 변경 완료", "비밀번호가 성공적으로 변경되었습니다.");
    return ResponseEntity.ok(response);
  }

  /**
   * 유저 프로필 조회
   *
   * @param userDetails 유저 정보
   * @return 상태코드, 응답 메시지, 응답 데이터
   */
  @GetMapping("/profile/user")
  public ResponseEntity<CommonResponse<ReadUserResponse>> readUserProfile(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReadUserResponse readUserResponse = userService.readUserProfile(userDetails);
    CommonResponse<ReadUserResponse> response = new CommonResponse<>(
        HttpStatus.OK.value(), "프로필 조회 완료", readUserResponse);
    return ResponseEntity.ok(response);
  }
}