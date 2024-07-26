package com.sparta.fltpleprojectbackend.trainer.controller;

import com.sparta.fltpleprojectbackend.trainer.dto.TrainerGetResponse;
import com.sparta.fltpleprojectbackend.trainer.service.TrainerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
