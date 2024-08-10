package com.sparta.fitpleprojectbackend.trainer.service;

import com.sparta.fitpleprojectbackend.trainer.dto.TrainerGetResponse;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerService {

  private final TrainerRepository trainerRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * 트레이너 전체 조회
   *
   * @return 트레이너 리스트
   */
  public List<TrainerGetResponse> getAllTrainers() {
    return trainerRepository.findAll().stream()
        .map(TrainerGetResponse::new)
        .toList();
  }
}
