package com.sparta.gymspartaprojectbackend.notification.repository;

import com.sparta.gymspartaprojectbackend.notification.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

}
