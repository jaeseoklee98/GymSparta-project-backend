package com.sparta.gymspartaprojectbackend.user.service;

import com.sparta.gymspartaprojectbackend.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserCleanupScheduler {

  private final UserRepository userRepository;

  public UserCleanupScheduler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   *
   * 매일 자정에 30일이 지난 탈퇴 유저를 삭제하는 스케줄러
   */
  @Scheduled(cron = "0 0 0 * * ?")
  public void cleanupDeletedUsers() {
    LocalDateTime now = LocalDateTime.now();
    userRepository.deleteAllByScheduledDeletionDateBefore(now);
  }
}