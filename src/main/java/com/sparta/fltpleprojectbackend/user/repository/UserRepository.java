package com.sparta.fltpleprojectbackend.user.repository;

import com.sparta.fltpleprojectbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
  Optional<User> findByPhoneNumber(String phoneNumber);
  void deleteAllByScheduledDeletionDateBefore(LocalDateTime now); // 추가된 메서드
}