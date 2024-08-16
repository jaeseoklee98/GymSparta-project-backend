package com.sparta.gymspartaprojectbackend.notification.repository;

import com.sparta.gymspartaprojectbackend.notification.entity.PaymentUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentUserNotificationRepository extends JpaRepository<PaymentUserNotification, Long> {
}