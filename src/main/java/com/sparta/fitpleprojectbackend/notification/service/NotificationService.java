package com.sparta.fitpleprojectbackend.notification.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.notification.dto.createAllNotificationDto;
import com.sparta.fitpleprojectbackend.notification.entity.AllNotification;
import com.sparta.fitpleprojectbackend.notification.exception.NotificationException;
import com.sparta.fitpleprojectbackend.notification.repository.AllNotificationRepository;
import com.sparta.fitpleprojectbackend.owner.entity.Owner;
import com.sparta.fitpleprojectbackend.owner.repository.OwnerRepository;
import com.sparta.fitpleprojectbackend.owner.service.OwnerService;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.store.entity.Store;
import com.sparta.fitpleprojectbackend.store.exception.StoreException;
import com.sparta.fitpleprojectbackend.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final Map<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();
  // 단일 스레드를 사용하여 작업하기 때문에 알림 전송을 다른 작업과 병행해서 실행 가능
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final AllNotificationRepository allnotificationRepository;
  private final StoreRepository storeRepository;

  @Transactional
  public void createNotification(Long storeId, UserDetailsImpl userDetails, createAllNotificationDto request) {
    Owner owner = userDetails.getOwner();
    Store store = storeRepository.findById(storeId).orElseThrow(()-> new NotificationException(ErrorType.NOT_FOUND_STORE));

    if (!store.getOwner().getId().equals(owner.getId())) {
      throw new NotificationException(ErrorType.INVALID_USER);
    }

    AllNotification allNotification = new AllNotification(request);

    allnotificationRepository.save(allNotification);

    sendUserAllNotification();
  }

  // sse 구독
  public SseEmitter createEmitter(Long userId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Sse 구독 시간에 대해서 관리 사실상 무한
    userEmitters.put(userId, emitter);

    emitter.onCompletion(() -> userEmitters.remove(userId)); // 클라이언트가 연결 정상적으로 완료했을 떄 호출되는 콜백
    emitter.onTimeout(() -> userEmitters.remove(userId));
    emitter.onError((e) -> userEmitters.remove(userId));

    return emitter;
  }

  @Transactional
  public void sendUserAllNotification(Long receiverId) {

    Notification notification = new Notification("알림 왔숑", receiverId); // 공지 id
    Notification savedNotification = notificationRepository.save(notification);
    sendRealTimeNotification(savedNotification);
  }

  private void sendRealTimeNotification(Notification notification) {
    SseEmitter emitter = userEmitters.get(notification.getUserId()); // 매장에 속해있는 로그인 하고 있는 유저들
    if (emitter != null) {
      excutor.excute(() -> {
        try {
          emitter.send(SseEmitter.event().name("notification").data(notification.getMessage()));
        } catch (Exception e) {
          // 예외 처리
        }
      });
    }
  }
}
