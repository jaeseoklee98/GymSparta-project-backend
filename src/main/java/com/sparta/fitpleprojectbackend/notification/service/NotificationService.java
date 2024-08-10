package com.sparta.fitpleprojectbackend.notification.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.notification.dto.createAllNotificationDto;
import com.sparta.fitpleprojectbackend.notification.entity.AllNotification;
import com.sparta.fitpleprojectbackend.notification.entity.UserAllNotification;
import com.sparta.fitpleprojectbackend.notification.exception.NotificationException;
import com.sparta.fitpleprojectbackend.notification.repository.AllNotificationRepository;
import com.sparta.fitpleprojectbackend.notification.repository.UserAllNotificationRepository;
import com.sparta.fitpleprojectbackend.owner.entity.Owner;
import com.sparta.fitpleprojectbackend.owner.repository.OwnerRepository;
import com.sparta.fitpleprojectbackend.owner.service.OwnerService;
import com.sparta.fitpleprojectbackend.payment.repository.PaymentRepository.PaymentRepository;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.store.entity.Store;
import com.sparta.fitpleprojectbackend.store.exception.StoreException;
import com.sparta.fitpleprojectbackend.store.repository.StoreRepository;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

  private final Map<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();
  private final ExecutorService executor = Executors.newSingleThreadExecutor(); // 단일 스레드를 사용하여 작업하기 때문에 알림 전송을 다른 작업과 병행해서 실행 가능
  private final AllNotificationRepository allnotificationRepository;
  private final StoreRepository storeRepository;
  private final PaymentRepository paymentRepository;
  private final UserAllNotificationRepository userAllNotificationRepository;

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

    AllNotification allNotification = new AllNotification(request);

    allnotificationRepository.save(allNotification);

    List<User> userList = paymentRepository.findUserByStoreId(storeId); // 매장에 한번이라도 결제 한 적이 있는 유저들

    // 리스트에 담긴 유저 수 만큼 반복문 실행
    for (User user : userList) {
      sendUserAllNotification(user, allNotification);
    }

  }

  /**
   * 실시간 공지 알림
   *
   * @param userId 로그인 한 유저의 ID
   * @return 구독권
   */
  public SseEmitter createEmitter(Long userId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Sse 구독 시간에 대해서 관리 사실상 무한
    userEmitters.put(userId, emitter);

    emitter.onCompletion(() -> userEmitters.remove(userId)); // 클라이언트가 연결 정상적으로 완료했을 떄 호출되는 콜백
    emitter.onTimeout(() -> userEmitters.remove(userId));
    emitter.onError((e) -> userEmitters.remove(userId));

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
    String message = allNotification.getStore().getStoreName() + "매장의 전체 공지가 추가되었습니다.";
    UserAllNotification notification = new UserAllNotification(message, user, allNotification);
    userAllNotificationRepository.save(notification);
    sendRealTimeNotification(notification);
  }

  /**
   * 매장 전체 공지 알림을 실시간으로 보내기
   *
   * @param notification 유저의 전체공지 알림
   */
  private void sendRealTimeNotification(UserAllNotification notification) {
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
   * 매일 아침 9시 만료 임박 회원권 조회하고 알림 보내기
   *
   * @param
   */

  /**
   * 매일 아침 9시 만료 임박 PT권 조회하고 알림 보내기
   *
   * @param
   */
}
