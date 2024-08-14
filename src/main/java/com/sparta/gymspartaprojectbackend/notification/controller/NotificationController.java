package com.sparta.gymspartaprojectbackend.notification.controller;

import com.sparta.gymspartaprojectbackend.notification.dto.NotificationDetailResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.NotificationSimpleResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.OwnerNotificationResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.UserAllNotificationResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.UserExpireNotificationResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.UserPaymentNotificationResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.createAllNotificationDto;
import com.sparta.gymspartaprojectbackend.notification.service.NotificationService;
import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
import java.util.List;
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
   * TODO: 로그인 시점에 구독하게 구독 시점 파악
   * @param userDetails 로그인 한 유저의 데이터
   * @return SseEmitter 실시간 알림 처리하는 구독권
   */
  @GetMapping("/userNotifications/stream")
  public SseEmitter streamOwnerUserNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return notificationService.createUserEmitter(userDetails.getUser().getId());
  }

  /**
   * 점주에게 실시간 알림을 보내기 위한 구독
   *
   * TODO: 로그인 시점에 구독하게 구독 시점 파악
   * @param userDetails 로그인 한 점주의 데이터
   * @return SseEmitter 실시간 알림 처리하는 구독권
   */
  @GetMapping("/ownerNotifications/stream")
  public SseEmitter streamOwnerNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return notificationService.createOwnerEmitter(userDetails.getOwner().getId());
  }

  /**
   * 매장 공지 목록 조회
   *
   * @param storeId 매장 ID
   * @return 매장 공지 목록
   */
  @GetMapping("/{storeId}/allNotification")
  public ResponseEntity<?> readNotification(@PathVariable Long storeId) {
    List<NotificationSimpleResponse> notificationSimpleResponseList = notificationService.readNotification(storeId);
    return ResponseEntity.ok(notificationSimpleResponseList);
  }

  /**
   * 매장 공지 상세 조회
   *
   * @param allNotificationId 공지 ID
   * @return 매장 공지 목록
   */
  @GetMapping("/allNotification/{allNotificationId}")
  public ResponseEntity<?> readNotificationDetails(@PathVariable Long allNotificationId) {
    NotificationDetailResponse response = notificationService.readNotificationDetail(allNotificationId);
    return ResponseEntity.ok(response);
  }

  // 점주 알림 목록 조회
  @GetMapping("/owner/notification")
  public ResponseEntity<?> readOwnerNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<OwnerNotificationResponse> response = notificationService.readOwnerNotification(userDetails);
    return ResponseEntity.ok(response);
  }

  // 유저 결제 알림 목록 조회
  @GetMapping("/user/notification/payment")
  public ResponseEntity<?> readUserPaymentNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<UserPaymentNotificationResponse> response = notificationService.readUserPaymentNotification(userDetails);
    return ResponseEntity.ok(response);
  }

  // 유저 만료 알림 목록 조회
  @GetMapping("/user/notification/Expire")
  public ResponseEntity<?> readUserExpireNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<UserExpireNotificationResponse> response = notificationService.readUserExpireNotification(userDetails);
    return ResponseEntity.ok(response);
  }

  // 유저 공지 알림 목록 조회
  @GetMapping("/user/notification/allNotification")
  public ResponseEntity<?> readUserAllNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<UserAllNotificationResponse> response = notificationService.readUserAllNotification(userDetails);
    return ResponseEntity.ok(response);
  }
}
