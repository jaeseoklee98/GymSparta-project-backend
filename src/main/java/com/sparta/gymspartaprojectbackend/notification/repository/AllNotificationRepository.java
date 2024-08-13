package com.sparta.gymspartaprojectbackend.notification.repository;

import com.sparta.gymspartaprojectbackend.notification.entity.AllNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllNotificationRepository extends JpaRepository<AllNotification, Long> {
  List<AllNotification> findByStoreId(Long storeId);

}
