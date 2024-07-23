package com.sparta.fltpleprojectbackend.user.repository;

import com.sparta.fltpleprojectbackend.user.entity.User;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByAccountId(String accountId);
  Optional<User> findByEmail(String email);
  Optional<User> findByPhoneNumber(String phoneNumber);

  void deleteAllByScheduledDeletionDateBefore(LocalDateTime now);
}