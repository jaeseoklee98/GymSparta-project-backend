package com.sparta.gymspartaprojectbackend.notification.repository;

import com.sparta.gymspartaprojectbackend.notification.entity.UserAllNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAllNotificationRepository extends JpaRepository<UserAllNotification, Long> {

  List<UserAllNotification> findByUserId(Long id);
}
