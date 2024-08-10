package com.sparta.fltpleprojectbackend.user.repository;

import com.sparta.fltpleprojectbackend.user.entity.User;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByAccountId(String accountId);
  Optional<User> findByEmail(String email);
  Optional<User> findByPhoneNumber(String phoneNumber);
  Optional<User> findByForeignerRegistrationNumber(String foreignerRegistrationNumber);
  Optional<User> findByResidentRegistrationNumber(String residentRegistrationNumber);
  Optional<User> findByAccountIdAndStatus(String accountId, String status);
  Optional<User> findByEmailAndStatus(String email, String status);
  Optional<User> findByPhoneNumberAndStatus(String phoneNumber, String status);

  void deleteAllByScheduledDeletionDateBefore(LocalDateTime now);
}