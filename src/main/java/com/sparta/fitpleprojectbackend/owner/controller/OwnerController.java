package com.sparta.fitpleprojectbackend.owner.controller;

import com.sparta.fitpleprojectbackend.common.CommonResponse;
import com.sparta.fitpleprojectbackend.jwtutil.JwtUtil;
import com.sparta.fitpleprojectbackend.owner.dto.OwnerSignupRequest;
import com.sparta.fitpleprojectbackend.owner.dto.ReadOwnerResponse;
import com.sparta.fitpleprojectbackend.owner.dto.UpdateOwnerPasswordRequest;
import com.sparta.fitpleprojectbackend.owner.dto.UpdateOwnerProfileRequest;
import com.sparta.fitpleprojectbackend.owner.service.OwnerService;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OwnerController {

  private final OwnerService ownerService;
  private final JwtUtil jwtUtil;

  public OwnerController(OwnerService ownerService, JwtUtil jwtUtil) {
    this.ownerService = ownerService;
    this.jwtUtil = jwtUtil;
  }

  /**
   * 점주 회원가입
   *
   * @param request 점주 회원가입 요청 정보
   * @return 상태코드와 응답 메시지
   */
  @PostMapping("/owners/signup")
  public ResponseEntity<CommonResponse<String>> signup(@RequestBody OwnerSignupRequest request) {
    try {
      ownerService.signup(request);
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.OK.value(), "회원가입 성공", "회원가입이 완료되었습니다.");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.BAD_REQUEST.value(), "회원가입 실패", "회원가입 실패" + e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
  }

  /**
   * 점주 회원탈퇴
   *
   * @param request HTTP 요청 객체
   * @return 상태코드와 응답 메시지
   */
  @DeleteMapping("/profile/owners/signout")
  public ResponseEntity<CommonResponse<String>> deleteOwner(HttpServletRequest request) {
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
      ownerService.deleteOwner(username);
      SecurityContextHolder.clearContext();
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.OK.value(), "회원탈퇴 성공", "회원탈퇴가 완료되었습니다.");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      CommonResponse<String> response = new CommonResponse<>(
          HttpStatus.BAD_REQUEST.value(), "회원탈퇴 실패", "회원탈퇴 실패: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
  }

  /**
   * 점주 프로필 조회
   *
   * @param userDetails 오너 정보
   * @return 상태코드, 응답 메시지, 응답 데이터
   */
  @GetMapping("/profile/owner")
  public ResponseEntity<CommonResponse<ReadOwnerResponse>> readOwnerProfile(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReadOwnerResponse readOwnerResponse = ownerService.readOwnerProfile(userDetails);
    CommonResponse<ReadOwnerResponse> response = new CommonResponse<>(
        HttpStatus.OK.value(), "프로필 조회 완료", readOwnerResponse);
    return ResponseEntity.ok(response);
  }

  /**
   * 점주 프로필 변경
   *
   * @param userDetails  점주 정보
   * @param ownerRequest 새 프로필 정보
   * @return statusCode: 200, message: 프로필 변경 완료
   */
  @PutMapping("/profile/owner")
  public ResponseEntity<CommonResponse<String>> updateOwnerProfile(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody UpdateOwnerProfileRequest ownerRequest) {

    ownerService.updateOwnerProfile(ownerRequest, userDetails);
    CommonResponse<String> response = new CommonResponse<>(
        HttpStatus.OK.value(), "프로필 변경 완료", "프로필이 성공적으로 변경되었습니다.");
    return ResponseEntity.ok(response);
  }

  /**
   * 점주 비밀번호 변경
   *
   * @param userDetails  점주 정보
   * @param ownerRequest 새 비밀번호 정보
   * @return statusCode: 200, message: 변경 완료
   */
  @PutMapping("/profile/owner/password")
  public ResponseEntity<CommonResponse<String>> updateOwnerPassword(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody UpdateOwnerPasswordRequest ownerRequest) {

    ownerService.updateOwnerPassword(ownerRequest, userDetails);
    CommonResponse<String> response = new CommonResponse<>(
        HttpStatus.OK.value(), "비밀번호 변경 완료", "비밀번호가 성공적으로 변경되었습니다.");
    return ResponseEntity.ok(response);
  }
}