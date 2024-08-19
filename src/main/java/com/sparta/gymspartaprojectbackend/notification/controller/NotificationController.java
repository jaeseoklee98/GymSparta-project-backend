package com.sparta.gymspartaprojectbackend.notification.controller;

import static com.sparta.gymspartaprojectbackend.enums.Role.OWNER;
import static com.sparta.gymspartaprojectbackend.enums.Role.USER;

import com.sparta.gymspartaprojectbackend.common.CommonResponse;
import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.notification.dto.NotificationDetailResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.NotificationSimpleResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.OwnerNotificationResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.UserNotificationResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.createAllNotificationDto;
import com.sparta.gymspartaprojectbackend.notification.exception.NotificationException;
import com.sparta.gymspartaprojectbackend.notification.service.NotificationService;
import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  /**
   * 점주 전체 공지 작성
   *
   * @param storeId 공지를 작성하는 매장의 ID`
   * @param request 공지 작성 내용
   * @param userDetails 로그인 한 점주의 데이터
   * @return 정상적으로 완료 했다는 메세지
   */
  @PostMapping("/{storeId}/allNotification")
  public ResponseEntity<CommonResponse<String>> createNotification(@PathVariable Long storeId, @RequestBody createAllNotificationDto request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    notificationService.createNotification(storeId, userDetails, request);
    CommonResponse<String> response = new CommonResponse<>( HttpStatus.OK.value(), "공지 작성 완료", "공지 작성이 성공적으로 완료되었습니다.");
    return ResponseEntity.ok(response);
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

  /**
   * 실시간 알림을 보내기 위한 구독
   *
   * @param userDetails 로그인 데이터
   * @return SseEmitter 실시간 알림 처리하는 구독권
   */
  @GetMapping("/stream")
  public SseEmitter streamNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    if (userDetails.getRole().equals(USER)) {
      return notificationService.createUserEmitter(userDetails.getUser().getId());

    } else if (userDetails.getRole().equals(OWNER)) {
      return notificationService.createOwnerEmitter(userDetails.getOwner().getId());

    } else {
      throw new NotificationException(ErrorType.NOT_FOUND_OWNER);
    }
  }

  // 점주, 유저 모든 알림 조회
  @GetMapping
  public ResponseEntity<?> readNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    if (userDetails.getRole().equals(USER)) {
      List<UserNotificationResponse> responseList = notificationService.readUserNotification(userDetails);
      return ResponseEntity.ok(responseList);
    } else if (userDetails.getRole().equals(OWNER)) {
      List<OwnerNotificationResponse> response = notificationService.readOwnerNotification(userDetails);
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.ok("트레이너는 알림이 없습니다.");
    }
  }

//  // 점주 알림 목록 조회
//  @GetMapping("/owner/notification")
//  public ResponseEntity<?> readOwnerNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//    List<OwnerNotificationResponse> response = notificationService.readOwnerNotification(userDetails);
//    return ResponseEntity.ok(response);
//  }

//  // 유저 결제 알림 목록 조회
//  @GetMapping("/user/notification/payment")
//  public ResponseEntity<?> readUserPaymentNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//    List<UserPaymentNotificationResponse> response = notificationService.readUserPaymentNotification(userDetails);
//    return ResponseEntity.ok(response);
//  }
//
//  // 유저 만료 알림 목록 조회
//  @GetMapping("/user/notification/Expire")
//  public ResponseEntity<?> readUserExpireNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//    List<UserExpireNotificationResponse> response = notificationService.readUserExpireNotification(userDetails);
//    return ResponseEntity.ok(response);
//  }
//
//  // 유저 공지 알림 목록 조회
//  @GetMapping("/user/notification/allNotification")
//  public ResponseEntity<?> readUserAllNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//    List<UserAllNotificationResponse> response = notificationService.readUserAllNotification(userDetails);
//    return ResponseEntity.ok(response);
//  }
}
