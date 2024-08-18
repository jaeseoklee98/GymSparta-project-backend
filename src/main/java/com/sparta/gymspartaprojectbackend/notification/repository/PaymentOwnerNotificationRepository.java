package com.sparta.gymspartaprojectbackend.notification.repository;

import com.sparta.gymspartaprojectbackend.notification.entity.PaymentOwnerNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOwnerNotificationRepository extends JpaRepository<PaymentOwnerNotification, Long> {
  List<PaymentOwnerNotification> findByOwnerId(Long ownerId);
}
