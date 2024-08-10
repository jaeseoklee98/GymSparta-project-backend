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

  /**
   * 점주 전체 공지 작성
   *
   * @param storeId 공지를 작성하는 매장의 ID
   * @param request 공지 작성 내용
   * @param userDetails 로그인 한 점주의 데이터
   * @return 정상적으로 완료 했다는 메세지
   */
  @PostMapping("/{storeId}/allNotification")
  public ResponseEntity<?> createNotification(@PathVariable Long storeId, @RequestBody createAllNotificationDto request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
  notificationService.createNotification(storeId, userDetails, request);
  return ResponseEntity.ok("공지 작성 완료");
  }

  /**
   * 유저에게 실시간 알림을 보내기 위한 구독
   *
   * @param userDetails 로그인 한 유저의 데이터
   * @return SseEmitter 실시간 알림 처리하는 구독권
   */
  @GetMapping("/notifications/stream")
  public SseEmitter streamNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return notificationService.createEmitter(userDetails.getUser().getId());
  }

}
