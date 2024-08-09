package com.sparta.fitpleprojectbackend.notification.controller;

import com.sparta.fitpleprojectbackend.notification.dto.createAllNotificationDto;
import com.sparta.fitpleprojectbackend.notification.entity.AllNotification;
import com.sparta.fitpleprojectbackend.notification.service.NotificationService;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  // 점주 공지 작성
  @PostMapping("/{storeId}/allNotification")
  public ResponseEntity<?> createNotification(@PathVariable Long storeId, @RequestBody createAllNotificationDto request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
  notificationService.createNotification(storeId, userDetails, request);
  return ResponseEntity.ok("공지 작성 완료");
  }

  // sse 구독
  @GetMapping("/notifications/stream")
  public SseEmitter streamNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return notificationService.createEmitter(userDetails.getUser().getId());
  }

}
