package com.sparta.gymspartaprojectbackend.notification.repository;

import com.sparta.gymspartaprojectbackend.notification.entity.PaymentOwnerNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOwnerNotificationRepository extends JpaRepository<PaymentOwnerNotification, Long> {
}