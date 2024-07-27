package com.sparta.fltpleprojectbackend.trainer.controller;

import com.sparta.fltpleprojectbackend.common.CommonResponse;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fltpleprojectbackend.trainer.dto.ReadTrainerResponse;
import com.sparta.fltpleprojectbackend.trainer.dto.TrainerGetResponse;
import com.sparta.fltpleprojectbackend.trainer.dto.UpdateTrainerPasswordRequest;
import com.sparta.fltpleprojectbackend.trainer.dto.UpdateTrainerProfileRequest;
import com.sparta.fltpleprojectbackend.trainer.service.TrainerService;
import com.sparta.fltpleprojectbackend.user.dto.ReadUserResponse;
import com.sparta.fltpleprojectbackend.user.dto.UpdatePasswordRequest;
import com.sparta.fltpleprojectbackend.user.dto.UpdateUserProfileRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TrainerController {

  private final TrainerService trainerService;

  /**.
   * 트레이너 전체 조회
   *
   * @return ok, 전체 트레이너 리스트
   */
  @GetMapping("/trainers")
  public ResponseEntity<List<TrainerGetResponse>> getAllTrainers() {
    return ResponseEntity.ok(trainerService.getAllTrainers());
  }

  /**.
   * 트레이너 프로필 조회
   *
   * @param userDetails 트레이너 정보
   * @return 상태코드, 응답 메시지, 응답 데이터
   */
  @GetMapping("/profile/trainer")
  public ResponseEntity<CommonResponse<ReadTrainerResponse>> readTrainerProfile (@AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReadTrainerResponse readTrainerProfile = trainerService.readTrainerProfile(userDetails);
    CommonResponse<ReadTrainerResponse> response = new CommonResponse<>(
      HttpStatus.OK.value(), "프로필 조회 완료", readTrainerProfile);
    return new  ResponseEntity<>(response, HttpStatus.OK);
  }

  /**.
   * 트레이너 프로필 변경
   *
   * @param userDetails 트레이너 정보
   * @param trainerRequest 새 프로필 정보
   * @return statusCode: 200, message: 프로필 변경 완료
   */
  @PutMapping("/profile/trainer")
  public ResponseEntity<?> updateTrainerProfile (
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @Valid @RequestBody UpdateTrainerProfileRequest trainerRequest) {
    // TODO: 리스폰 형식 컨벤션에 맞추기
    trainerService.updateTrainerProfile(trainerRequest, userDetails);

    return ResponseEntity.ok("프로필 변경 완료");
  }

  /**.
   * 트레이너 비밀번호 변경
   *
   * @param userDetails 트레이너 정보
   * @param trainerRequest 새 비밀번호 정보
   * @return statusCode: 200, message: 변경 완료
   */
  @PutMapping("/profile/trainer/password")
  public ResponseEntity<?> updateTrainerPassword (
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @Valid @RequestBody UpdateTrainerPasswordRequest trainerRequest) {
    // TODO: 리스폰 형식 컨벤션에 맞추기
    trainerService.updateTrainerPassword(trainerRequest, userDetails);

    return ResponseEntity.ok("비밀번호 변경 완료");
  }
}
