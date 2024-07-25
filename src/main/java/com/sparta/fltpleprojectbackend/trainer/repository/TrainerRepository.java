package com.sparta.fltpleprojectbackend.trainer.repository;

import com.sparta.fltpleprojectbackend.trainer.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
  Optional<Trainer> findByAccountId(String accountId);
  Optional<Trainer> findByEmailAndTrainerStatus(String email, String status);
  Optional<Trainer> findByTrainerPhoneNumberAndTrainerStatus(String phoneNumber, String status);
}