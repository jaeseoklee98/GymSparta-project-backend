package com.sparta.fltpleprojectbackend.owner.controller;

import com.sparta.fltpleprojectbackend.common.CommonResponse;
import com.sparta.fltpleprojectbackend.jwtutil.JwtUtil;
import com.sparta.fltpleprojectbackend.owner.dto.OwnerSignupRequest;
import com.sparta.fltpleprojectbackend.owner.dto.ReadOwnerResponse;
import com.sparta.fltpleprojectbackend.owner.dto.UpdateOwnerPasswordRequest;
import com.sparta.fltpleprojectbackend.owner.dto.UpdateOwnerProfileRequest;
import com.sparta.fltpleprojectbackend.owner.service.OwnerService;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fltpleprojectbackend.trainer.dto.TrainerGetResponse;
import com.sparta.fltpleprojectbackend.trainer.service.TrainerService;
import com.sparta.fltpleprojectbackend.user.dto.ResponseMessage;
import com.sparta.fltpleprojectbackend.user.dto.UpdatePasswordRequest;
import com.sparta.fltpleprojectbackend.user.dto.UpdateUserProfileRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  public OwnerController(OwnerService ownerService, JwtUtil jwtUtil, TrainerService trainerService) {
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

  /**.
   * 자신의 매장의 트레이너 리스트
   *
   * @param userDetails 오너 정보
   * @return 상태코드, 응답 메시지, 응답 데이터
   */
  @GetMapping("/trainer/myTrainer")
  public ResponseEntity<CommonResponse<List<TrainerGetResponse>>> getMyTrainer (@AuthenticationPrincipal UserDetailsImpl userDetails) {

    List<TrainerGetResponse> trainerList = ownerService.getAllMyTrainers(userDetails);

    CommonResponse<List<TrainerGetResponse>> response = new CommonResponse<>(HttpStatus.OK.value(),
      "모든 매장의 모든 트레이너 조회", trainerList);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**.
   * 오너 프로필 조회
   *
   * @param userDetails 오너 정보
   * @return 상태코드, 응답 메시지, 응답 데이터
   */
  @GetMapping("/profile/owner")
  public ResponseEntity<CommonResponse<ReadOwnerResponse>> readOwnerProfile (@AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReadOwnerResponse ReadOwnerResponse = ownerService.readOwnerProfile(userDetails);
    CommonResponse<ReadOwnerResponse> response = new CommonResponse<>(
      HttpStatus.OK.value(), "프로필 조회 완료", ReadOwnerResponse);
    return new  ResponseEntity<>(response, HttpStatus.OK);
  }

  /**.
   * 점주 프로필 변경
   *
   * @param userDetails 점주 정보
   * @param ownerRequest 새 프로필 정보
   * @return statusCode: 200, message: 프로필 변경 완료
   */
  @PutMapping("/profile/owner")
  public ResponseEntity<?> updateOwnerProfile (
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @Valid @RequestBody UpdateOwnerProfileRequest ownerRequest) {
    // TODO: 리스폰 형식 컨벤션에 맞추기
    ownerService.updateOwnerProfile(ownerRequest, userDetails);

    return ResponseEntity.ok("프로필 변경 완료");
  }

  /**.
   * 점주 비밀번호 변경
   *
   * @param userDetails 점주 정보
   * @param ownerRequest 새 비밀번호 정보
   * @return statusCode: 200, message: 변경 완료
   */
  @PutMapping("/profile/owner/password")
  public ResponseEntity<?> updateOwnerPassword (
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @Valid @RequestBody UpdateOwnerPasswordRequest ownerRequest) {
    // TODO: 리스폰 형식 컨벤션에 맞추기
    ownerService.updateOwnerPassword(ownerRequest, userDetails);

    return ResponseEntity.ok("비밀번호 변경 완료");
  }
}