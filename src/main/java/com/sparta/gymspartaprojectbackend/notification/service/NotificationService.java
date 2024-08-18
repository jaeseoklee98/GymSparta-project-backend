package com.sparta.gymspartaprojectbackend.notification.service;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.notification.dto.NotificationDetailResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.NotificationSimpleResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.OwnerNotificationResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.UserAllNotificationResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.UserExpireNotificationResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.UserNotificationResponse;
import com.sparta.gymspartaprojectbackend.notification.dto.createAllNotificationDto;
import com.sparta.gymspartaprojectbackend.notification.entity.AllNotification;
import com.sparta.gymspartaprojectbackend.notification.entity.PaymentOwnerNotification;
import com.sparta.gymspartaprojectbackend.notification.entity.PaymentUserNotification;
import com.sparta.gymspartaprojectbackend.notification.entity.UserAllNotification;
import com.sparta.gymspartaprojectbackend.notification.entity.UserNotification;
import com.sparta.gymspartaprojectbackend.notification.exception.NotificationException;
import com.sparta.gymspartaprojectbackend.notification.repository.AllNotificationRepository;
import com.sparta.gymspartaprojectbackend.notification.repository.PaymentOwnerNotificationRepository;
import com.sparta.gymspartaprojectbackend.notification.repository.PaymentUserNotificationRepository;
import com.sparta.gymspartaprojectbackend.notification.repository.UserAllNotificationRepository;
import com.sparta.gymspartaprojectbackend.notification.repository.UserNotificationRepository;
import com.sparta.gymspartaprojectbackend.owner.entity.Owner;
import com.sparta.gymspartaprojectbackend.payment.entity.Payment;
import com.sparta.gymspartaprojectbackend.payment.repository.PaymentRepository;
import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
import com.sparta.gymspartaprojectbackend.store.entity.Store;
import com.sparta.gymspartaprojectbackend.store.repository.StoreRepository;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

  private final Map<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();
  private final Map<Long, SseEmitter> ownerEmitters = new ConcurrentHashMap<>();
  private final ExecutorService executor = Executors.newSingleThreadExecutor(); // 단일 스레드를 사용하여 작업하기 때문에 알림 전송을 다른 작업과 병행해서 실행 가능
  private final AllNotificationRepository allnotificationRepository;
  private final StoreRepository storeRepository;
  private final PaymentRepository paymentRepository;
  private final UserAllNotificationRepository userAllNotificationRepository;
  private final UserNotificationRepository userNotificationRepository;
  private final PaymentOwnerNotificationRepository paymentOwnerNotificationRepository;
  private final PaymentUserNotificationRepository paymentUserNotificationRepository;

  /**
   * 점주 전체 공지 작성
   *
   * @param storeId 공지를 작성하는 매장의 ID
   * @param request 공지 작성 내용
   * @param userDetails 로그인 한 점주의 데이터
   */
  @Transactional
  public void createNotification(Long storeId, UserDetailsImpl userDetails, createAllNotificationDto request) {
    Owner owner = userDetails.getOwner();
    Store store = storeRepository.findById(storeId).orElseThrow(()-> new NotificationException(ErrorType.NOT_FOUND_STORE));

    if (!store.getOwner().getId().equals(owner.getId())) {
      throw new NotificationException(ErrorType.INVALID_USER);
    }

    AllNotification allNotification = new AllNotification(request, store);

    allnotificationRepository.save(allNotification);

    List<User> userList = paymentRepository.findUserByStoreId(storeId); // 매장에 한번이라도 결제 한 적이 있는 유저들

    // 리스트에 담긴 유저 수 만큼 반복문 실행
    for (User user : userList) {
      sendUserAllNotification(user, allNotification);
    }

  }

  /**
   * 실시간 유저 알림 구독
   *
   * @param userId 로그인 한 유저의 ID
   * @return 구독권
   */
  public SseEmitter createUserEmitter(Long userId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Sse 구독 시간에 대해서 관리 사실상 무한
    userEmitters.put(userId, emitter);

    emitter.onCompletion(() -> userEmitters.remove(userId)); // 클라이언트가 연결 정상적으로 완료했을 떄 호출되는 콜백
    emitter.onTimeout(() -> userEmitters.remove(userId));
    emitter.onError((e) -> userEmitters.remove(userId));

    return emitter;
  }

  /**
   * 실시간 유저 알림 구독
   *
   * @param ownerId 로그인 한 유저의 ID
   * @return 구독권
   */
  public SseEmitter createOwnerEmitter(Long ownerId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Sse 구독 시간에 대해서 관리 사실상 무한
    ownerEmitters.put(ownerId, emitter);

    emitter.onCompletion(() -> userEmitters.remove(ownerId)); // 클라이언트가 연결 정상적으로 완료했을 떄 호출되는 콜백
    emitter.onTimeout(() -> userEmitters.remove(ownerId));
    emitter.onError((e) -> userEmitters.remove(ownerId));

    return emitter;
  }

  /**
   * 매장 전체 공지 알림을 저장
   *
   * @param user 공지를 작성한 매장의 유저
   * @param allNotification 공지 데이터
   */
  @Transactional
  public void sendUserAllNotification(User user, AllNotification allNotification) {
    String title = allNotification.getStore().getStoreName() + "매장의 전체 공지알림";
    String message = allNotification.getStore().getStoreName() + "매장의 전체 공지가 추가되었습니다.";
    UserAllNotification notification = new UserAllNotification(title ,message, user, allNotification);
    userAllNotificationRepository.save(notification);
    sendRealTimeUserAllNotification(notification);
  }

  /**
   * 매장 전체 공지 알림을 실시간으로 보내기
   *
   * @param notification 유저의 전체공지 알림
   */
  public void sendRealTimeUserAllNotification(UserAllNotification notification) {
    SseEmitter emitter = userEmitters.get(notification.getUser().getId());
    if (emitter != null) {
      executor.execute(() -> {
        try {
          emitter.send(SseEmitter.event().name("notification").data(notification.getMessage()));
        } catch (Exception e) {
          throw new NotificationException(ErrorType.Notification_delivery_failed);
        }
      });
    }
  }

  /**
   * 매일 아침 9시 만료 임박 회원권 조회
   *
   */
  @Scheduled(cron = "0 0 9 * * ?")
  public void getMembershipExpire() {
    LocalDateTime today = LocalDateTime.now();
    LocalDateTime twoDaysLater = today.plusDays(2);

    List<User> ExpireMembershipList = paymentRepository.findMembershipExpiringSoon(today, twoDaysLater);

    for (User user : ExpireMembershipList) {
      sendMembershipExpiredNotification(user);
    }
  }

  /**
   * 매일 아침 9시 만료 임박 PT권 조회
   *
   */
  @Scheduled(cron = "0 0 9 * * ?")
  public void getPtExpire() {
    LocalDateTime today = LocalDateTime.now();
    LocalDateTime twoDaysLater = today.plusDays(2);

    // 만료 pt에 해당되는 '유저' 를 보내면 됨 그럼 pt 인지 확인하고 그게 저 위의 기간에 들어가는지를 결제에서 판단 하면 됨
    List<User> ExpirePtList = paymentRepository.findPtExpiringSoon(today, twoDaysLater);

    for (User user : ExpirePtList) {
      sendPtExpiredNotification(user);
    }
  }

  /**
   * 매일 아침 9시 만료 임박 PT권 알림 보내기
   *
   */
  private void sendPtExpiredNotification(User user) {
    String title = user.getUserName()+"님의 pt권 만료";
    String message = "보유중인 pt 권이 곧 만료됩니다.";
    UserNotification notification = new UserNotification(title, message, user);
    userNotificationRepository.save(notification);
    sendRealTimeUserNotification(notification);
  }

  /**
   * 매일 아침 9시 만료 임박 회원권 알림 보내기
   */
  private void sendMembershipExpiredNotification(User user) {
    String title = user.getUserName()+"님의 회원권 만료";
    String message = "보유중인 회원권이 곧 만료됩니다.";
    UserNotification notification = new UserNotification(title, message, user);
    userNotificationRepository.save(notification);
    sendRealTimeUserNotification(notification);
  }

  /**
   * 매일 아침 9시 만료 임박 PT권, 회원권 알림 보내기 (SSE)
   *
   */
  public void sendRealTimeUserNotification(UserNotification notification) {
    SseEmitter emitter = userEmitters.get(notification.getUser().getId());
    if (emitter != null) {
      executor.execute(() -> {
        try {
          emitter.send(SseEmitter.event().name(notification.getTitle()).data(notification.getMessage()));
        } catch (Exception e) {
          throw new NotificationException(ErrorType.Notification_delivery_failed);
        }
      });
    }
  }


  /**
   * 결제 완료 알림 보내기
   *
   */
  public void sendPaymentNotification(Payment payment) {
    String title = payment.getStore().getStoreName() + "매장의 결제 알림";
    String message = payment.getStore().getStoreName() + "매장의 상품 결제가 완료 되었습니다.";
    PaymentUserNotification userNotification = new PaymentUserNotification(title, message, payment);
    PaymentOwnerNotification ownerNotification = new PaymentOwnerNotification(title, message, payment);

    sendRealTimePaymentUserNotification(userNotification);
    sendRealTimePaymentOwnerNotification(ownerNotification);
  }

  /**
   * 결제 유저 알림 보내기 (SSE)
   *
   */
  public void sendRealTimePaymentUserNotification(PaymentUserNotification notification) {
    SseEmitter emitter = userEmitters.get(notification.getUser().getId());
    if (emitter != null) {
      executor.execute(() -> {
        try {
          emitter.send(SseEmitter.event().name(notification.getTitle()).data(notification.getMessage()));
        } catch (Exception e) {
          throw new NotificationException(ErrorType.Notification_delivery_failed);
        }
      });
    }
  }

  /**
   * 결제 점주 알림 보내기 (SSE)
   *
   */
  public void sendRealTimePaymentOwnerNotification(PaymentOwnerNotification notification) {
    SseEmitter emitter = ownerEmitters.get(notification.getOwner().getId());
    if (emitter != null) {
      executor.execute(() -> {
        try {
          emitter.send(SseEmitter.event().name("notification").data(notification.getMessage()));
        } catch (Exception e) {
          throw new NotificationException(ErrorType.Notification_delivery_failed);
        }
      });
    }
  }

  /**
   * 매장 공지 목록 조회
   * @param storeId 매장 id
   * @return 공지 목록
   */
  public List<NotificationSimpleResponse> readNotification(Long storeId) {
    List<AllNotification> allNotificationList = allnotificationRepository.findByStoreId(storeId);

    List<NotificationSimpleResponse> responseList = allNotificationList.stream()
      .map(NotificationSimpleResponse::new)
      .toList();

    return responseList;
  }

  /**
   * 매장 공지 상세 조회
   * @param allNotificationId 공지 id
   * @return 공지 상세
   */
  public NotificationDetailResponse readNotificationDetail(Long allNotificationId) {
    AllNotification allNotification = allnotificationRepository.findById(allNotificationId).orElseThrow(() -> new NotificationException(ErrorType.NOT_FOUND_NOTIFICATION));
    return new NotificationDetailResponse(allNotification);
  }

  /**
   * 점장 알림 조회
   * @param userDetails 로그인한 점장
   * @return 점장 알림
   */
  public List<OwnerNotificationResponse> readOwnerNotification(UserDetailsImpl userDetails) {
    List<PaymentOwnerNotification> ownerNotificationList = paymentOwnerNotificationRepository.findByOwnerId(userDetails.getOwner().getId());

    List<OwnerNotificationResponse> responseList = ownerNotificationList.stream()
      .map(notification -> new OwnerNotificationResponse(notification.getTitle(), notification.getMessage()))
      .toList();

    return responseList;
  }

  /**
   * 유저 알림 조회 - 결제, 알림, 만료
   * @param userDetails 로그인한 점장
   * @return 유저 알림
   */
  public List<UserNotificationResponse> readUserNotification(UserDetailsImpl userDetails) {
    List<UserNotificationResponse> notificationResponseList = new ArrayList<>();
    
    // 결제 알림
    List<PaymentUserNotification> paymentNotificationList = paymentUserNotificationRepository.findByUserId(userDetails.getUser().getId());

    notificationResponseList.addAll(paymentNotificationList.stream()
      .map(notification -> new UserNotificationResponse(notification.getTitle(), notification.getMessage()))
      .toList());
    
    // 만료 알림
    List<UserNotification> expireNotificationList = userNotificationRepository.findByUserId(userDetails.getUser().getId());

    notificationResponseList.addAll(expireNotificationList.stream()
      .map(notification -> new UserNotificationResponse(notification.getTitle(), notification.getMessage()))
      .toList());


    // 공지 알림
    List<UserAllNotification> AllNotificationList = userAllNotificationRepository.findByUserId(userDetails.getUser().getId());

    notificationResponseList.addAll(AllNotificationList.stream()
      .map(notification -> new UserNotificationResponse(notification.getTitle(), notification.getMessage()))
      .toList());


    return notificationResponseList;
  }



//  /**
//   * 유저 알림 조회 - 결제
//   * @param userDetails 로그인한 유저
//   * @return 유저 알림
//   */
//  public List<UserPaymentNotificationResponse> readUserPaymentNotification(UserDetailsImpl userDetails) {
//    List<PaymentUserNotification> userNotificationList = paymentUserNotificationRepository.findByUserId(userDetails.getUser().getId());
//    List<UserPaymentNotificationResponse> responseList = userNotificationList.stream()
//      .map(notification -> new UserPaymentNotificationResponse(notification.getTitle(), notification.getMessage()))
//      .toList();
//
//    return responseList;
//}

///**
// * 유저 알림 조회 - 만료
// * @param userDetails 로그인한 유저
// * @return 유저 알림
// */
//public List<UserExpireNotificationResponse> readUserExpireNotification(UserDetailsImpl userDetails) {
//  List<UserNotification> userNotificationList = userNotificationRepository.findByUserId(userDetails.getUser().getId());
//
//  List<UserExpireNotificationResponse> responseList = userNotificationList.stream()
//    .map(notification -> new UserExpireNotificationResponse(notification.getTitle(), notification.getMessage()))
//    .toList();
//
//  return responseList;
//}

///**
// * 유저 알림 조회 - 공지
// * @param userDetails 로그인한 유저
// * @return 유저 알림
// */
//public List<UserAllNotificationResponse> readUserAllNotification(UserDetailsImpl userDetails) {
//  List<UserAllNotification> userAllNotificationList = userAllNotificationRepository.findByUserId(userDetails.getUser().getId());
//
//  List<UserAllNotificationResponse> responseList = userAllNotificationList.stream()
//    .map(notification -> new UserAllNotificationResponse(notification.getMessage()))
//    .toList();
//
//  return responseList;
//}
}
