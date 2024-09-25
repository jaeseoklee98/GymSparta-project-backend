package com.sparta.gymspartaprojectbackend.trainer.repository;

import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long>, TrainerRepositoryQuery {
  Optional<Trainer> findByAccountId(String accountId);

  Optional<Trainer> findByEmailAndTrainerStatus(String email, String status);

  Optional<Trainer> findByTrainerPhoneNumberAndTrainerStatus(String phoneNumber, String status);

  Optional<Trainer> findByAccountIdAndTrainerStatus(String username, String active);

  List<Trainer> findByStoreIdAndTrainerStatus(Long store_id, String trainerStatus);
}