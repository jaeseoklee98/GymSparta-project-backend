package com.sparta.gymspartaprojectbackend.payment.service;

import com.sparta.gymspartaprojectbackend.enums.ErrorType;
import com.sparta.gymspartaprojectbackend.exception.CustomException;
import com.sparta.gymspartaprojectbackend.notification.service.NotificationService;
import com.sparta.gymspartaprojectbackend.payment.dto.PaymentRequest;
import com.sparta.gymspartaprojectbackend.payment.dto.PaymentResponse;
import com.sparta.gymspartaprojectbackend.payment.dto.PaymentUpdateRequest;
import com.sparta.gymspartaprojectbackend.payment.entity.Payment;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentType;
import com.sparta.gymspartaprojectbackend.payment.enums.PtTimes;
import com.sparta.gymspartaprojectbackend.payment.repository.PaymentRepository;
import com.sparta.gymspartaprojectbackend.trainer.entity.Trainer;
import com.sparta.gymspartaprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import com.sparta.gymspartaprojectbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
  private final PaymentRepository paymentRepository;
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;
  private final NotificationService notificationService;

  public PaymentService(PaymentRepository paymentRepository, TrainerRepository trainerRepository, UserRepository userRepository,
    NotificationService notificationService) {
    this.paymentRepository = paymentRepository;
    this.trainerRepository = trainerRepository;
    this.userRepository = userRepository;
    this.notificationService = notificationService;
  }

  /**
   * 트레이너와 유저 검증
   *
   * @param trainerId 트레이너 ID
   * @param userId    유저 ID
   * @throws CustomException 트레이너 또는 유저가 존재하지 않는 경우
   */
  public void validateTrainerAndUser(Long trainerId, Long userId) {
    Trainer trainer = trainerRepository.findById(trainerId)
        .orElseThrow(() -> new CustomException(ErrorType.TRAINER_NOT_FOUND));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
  }

  /**
   * 결제 정보 임시 저장
   *
   * @param request 결제 요청 정보
   * @return 저장된 결제 정보
   */
  @Transactional
  public Payment savePayment(PaymentRequest request) {
    validateTrainerAndUser(request.getTrainerId(), request.getUserId());

    Trainer trainer = trainerRepository.findById(request.getTrainerId())
        .orElseThrow(() -> new CustomException(ErrorType.TRAINER_NOT_FOUND));
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

    PtTimes ptTimes = request.getPtTimes() != null ? request.getPtTimes() : PtTimes.TEN_TIMES;

    // ProductType에 따른 결제 로직
    Payment payment;
    switch (request.getProductType()) {
      case PT_SESSION:
        payment = new Payment(trainer, user, null, ptTimes, request.getProductType(),
            request.getPaymentType(), request.getAmount(),
            PaymentStatus.PENDING, LocalDateTime.now(),
            LocalDateTime.now().plusDays(ptTimes.getTimes() / 30),
            request.isMembership());
        // PT 세션 결제에 대한 추가 처리
        logger.info("PT 세션 결제가 저장되었습니다: " + payment.getAmount());
        sendNotification(user, "PT 세션 결제가 완료되었습니다.");
        break;
      case MEMBERSHIP:
        payment = new Payment(trainer, user, null, null, request.getProductType(),
            request.getPaymentType(), request.getAmount(),
            PaymentStatus.PENDING, LocalDateTime.now(),
            LocalDateTime.now().plusYears(1), // 멤버십은 1년 기간
            request.isMembership());
        // 멤버십 결제에 대한 추가 처리
        logger.info("멤버십 결제가 저장되었습니다: " + payment.getAmount());
        sendNotification(user, "멤버십 결제가 완료되었습니다.");
        break;
      default:
        throw new CustomException(ErrorType.INVALID_PRODUCT_TYPE);
    }

    return paymentRepository.save(payment);
  }

  /**
   * 결제 승인 및 상태 업데이트
   *
   * @param paymentId 결제 ID
   * @param paymentType 결제 수단
   * @return 승인된 결제 정보
   */
  @Transactional
  public Payment approvePayment(Long paymentId, PaymentType paymentType) {

    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));

    if (payment.getPaymentStatus() == PaymentStatus.APPROVED) {
      throw new CustomException(ErrorType.PAYMENT_ALREADY_COMPLETED);
    }

    if (payment.getPaymentStatus() == PaymentStatus.CANCELED) {
      throw new CustomException(ErrorType.PAYMENT_ALREADY_CANCELED);
    }

    // ProductType에 따른 승인 후처리
    switch (payment.getProductType()) {
      case PT_SESSION:
        // 트레이너의 PT 세션 횟수 증가
        trainerRepository.findById(payment.getTrainer().getTrainerId())
            .ifPresent(trainer -> {
              trainer.increaseSessionCount(payment.getPtTimes().getTimes());
              trainerRepository.save(trainer);
            });
        // PT 세션 예약 가능 상태 업데이트
        updateSessionAvailability(payment.getTrainer(), payment.getPtTimes().getTimes());
        sendNotification(payment.getUser(), "PT 세션 결제가 승인되었습니다.");
        break;
      case MEMBERSHIP:
        // 유저의 멤버십 상태 활성화
        userRepository.findById(payment.getUser().getUserId())
            .ifPresent(user -> {
              user.activateMembership(LocalDateTime.now().plusYears(1));
              userRepository.save(user);
            });
        // 멤버십 혜택 적용 및 알림
        applyMembershipBenefits(payment.getUser());
        sendNotification(payment.getUser(), "멤버십이 활성화되었습니다.");
        break;
      default:
        throw new CustomException(ErrorType.INVALID_PRODUCT_TYPE);
    }

    payment.setPaymentStatus(PaymentStatus.APPROVED);
    payment.setPaymentType(paymentType);

    return paymentRepository.save(payment);
  }

  /**
   * 환불 요청 처리
   *
   * @param paymentId 환불할 결제의 ID
   * @throws CustomException 해당 결제를 찾을 수 없거나 환불이 실패한 경우 예외 발생
   */
  @Transactional
  public void processRefund(Long paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(ErrorType.REFUND_REQUEST_NOT_FOUND));

    if (payment.getPaymentStatus() == PaymentStatus.CANCELED) {
      throw new CustomException(ErrorType.REFUND_ALREADY_PROCESSED);
    }

    // 환불 가능한 기간이 지났는지 확인
    if (payment.getPaymentDate().isBefore(LocalDateTime.now().minusDays(30))) {
      throw new CustomException(ErrorType.REFUND_WINDOW_EXPIRED);
    }

    // 결제 제공자의 환불 API 호출 가정
    boolean refundSuccess = refundPaymentProvider(payment);

    if (refundSuccess) {
      payment.setPaymentStatus(PaymentStatus.CANCELED);

      // ProductType에 따른 환불 후처리
      switch (payment.getProductType()) {
        case PT_SESSION:
          // 트레이너의 PT 세션 횟수 감소
          trainerRepository.findById(payment.getTrainer().getTrainerId())
              .ifPresent(trainer -> {
                trainer.decreaseSessionCount(payment.getPtTimes().getTimes());
                trainerRepository.save(trainer);
              });
          sendNotification(payment.getUser(), "PT 세션 환불이 처리되었습니다.");
          break;
        case MEMBERSHIP:
          // 유저의 멤버십 비활성화
          userRepository.findById(payment.getUser().getUserId())
              .ifPresent(user -> {
                user.deactivateMembership();
                userRepository.save(user);
              });
          // 모든 예약 취소 및 알림 발송
          cancelMembershipReservations(payment.getUser());
          sendNotification(payment.getUser(), "멤버십 환불이 처리되었습니다.");
          break;
        default:
          throw new CustomException(ErrorType.INVALID_PRODUCT_TYPE);
      }

      paymentRepository.save(payment);
    } else {
      throw new CustomException(ErrorType.REFUND_PROCESSING_ERROR);
    }
  }

  /**
   * 사용자가 PT 횟수를 선택
   *
   * @param selectedTimes 선택된 PT 횟수
   * @return 선택된 PT 횟수 객체
   * @throws CustomException 잘못된 선택인 경우
   */
  public PtTimes selectPtTimes(String selectedTimes) {
    Optional<PtTimes> ptTimeArray = Arrays.stream(PtTimes.values())
        .filter(ptTimes -> ptTimes.name().equalsIgnoreCase(selectedTimes))
        .findFirst();

    if (ptTimeArray.isEmpty()) {
      throw new CustomException(ErrorType.INVALID_INPUT);
    }

    PtTimes ptTimes = ptTimeArray.get();
    logger.info("선택된 PT 횟수: ", ptTimes);

    return ptTimes;
  }

  /**
   * 결제 갱신
   *
   * @param paymentId 갱신할 결제의 ID
   * @param updateRequest 결제 갱신 요청 정보
   * @return 갱신된 결제 정보
   */
  @Transactional
  public Payment updatePayment(Long paymentId, PaymentUpdateRequest updateRequest) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));

    // 상태 변경 시 예외 처리
    if (updateRequest.getStatus() != null) {
      if (payment.getPaymentStatus() == PaymentStatus.APPROVED || payment.getPaymentStatus() == PaymentStatus.CANCELED) {
        throw new CustomException(ErrorType.PAYMENT_ALREADY_COMPLETED);
      }
      if (updateRequest.getStatus() == PaymentStatus.CANCELED && payment.getPaymentStatus() != PaymentStatus.PENDING) {
        throw new CustomException(ErrorType.INVALID_PAYMENT_STATUS);
      }
      payment.setPaymentStatus(updateRequest.getStatus());
    }

    // 결제 금액 변경 시 예외 처리
    if (updateRequest.getAmount() != null) {
      if (updateRequest.getAmount() <= 0) {
        throw new CustomException(ErrorType.INVALID_PAYMENT_AMOUNT);
      }
      payment.setAmount(updateRequest.getAmount());
    }

    // 결제 수단 변경 시 예외 처리
    if (updateRequest.getPaymentType() != null) {
      if (payment.getPaymentStatus() == PaymentStatus.APPROVED || payment.getPaymentStatus() == PaymentStatus.CANCELED) {
        throw new CustomException(ErrorType.PAYMENT_TYPE_NOT_UPDATABLE);
      }
      payment.setPaymentType(updateRequest.getPaymentType());
    }

    return paymentRepository.save(payment);
  }

  /**
   * 결제 상태 조회
   *
   * @param paymentId 조회할 결제의 ID
   * @return 결제 상태
   * @throws CustomException 결제를 찾을 수 없는 경우 발생
   */
  public PaymentStatus inquirePaymentStatus(Long paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));

    // 결제가 아직 처리 중인 경우 상태를 조회할 수 없도록 처리
    if (payment.getPaymentStatus() == PaymentStatus.PENDING) {
      throw new CustomException(ErrorType.PAYMENT_IN_PROCESS);
    }

    return payment.getPaymentStatus();
  }

  /**
   * 선택한 결제 수단으로 결제 처리
   *
   * @param paymentId 결제 ID
   * @param paymentType 선택한 결제 수단
   * @return 결제 결과 메시지
   */
  @Transactional
  public String processPaymentWithSelectedType(Long paymentId, PaymentType paymentType) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));

    if (payment.getPaymentStatus() != PaymentStatus.PENDING) {
      throw new CustomException(ErrorType.INVALID_PAYMENT_STATUS);
    }

    // 결제 수단에 따른 API 호출 로직
    switch (paymentType) {
      case CREDIT_CARD:
        // 여기에 신용카드 결제 API 호출 로직 추가
        break;
      case DEBIT_CARD:
        // 여기에 직불카드 결제 API 호출 로직 추가
        break;
      case CASH:
        // 여기에 현금 결제 처리 로직 추가
        break;
      default:
        throw new CustomException(ErrorType.UNSUPPORTED_PAYMENT_METHOD);
    }

    payment.setPaymentType(paymentType);
    payment.setPaymentStatus(PaymentStatus.APPROVED); // 결제가 성공했다고 가정
    paymentRepository.save(payment);

    return "Payment processed successfully with " + paymentType.getTypes();
  }

  /**
   * 결제 완료 후, 결제 내역 저장
   */
  @Transactional
  public Payment completePayment(Long paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));

    if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
      throw new CustomException(ErrorType.PAYMENT_ALREADY_COMPLETED);
    }

    // 결제 상태를 완료로 업데이트
    payment.setPaymentStatus(PaymentStatus.COMPLETED);
    notificationService.sendPaymentNotification(payment);
    return paymentRepository.save(payment);
  }

  /**
   * 유저가 자신의 결제 내역을 확인하는 기능
   *
   * @param userId 확인할 유저의 ID
   * @return 해당 유저의 결제 내역 리스트
   */
  public List<PaymentResponse> getUserPaymentHistory(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

    List<Payment> payments = paymentRepository.findAllByUser_Id(userId);

    // 결제 내역이 없을 때 예외 처리
    if (payments.isEmpty()) {
      throw new CustomException(ErrorType.PAYMENT_RECORD_NOT_FOUND);
    }

    // 결제 내역을 DTO로 변환하여 반환
    return payments.stream()
        .map(PaymentResponse::fromEntity)
        .collect(Collectors.toList());
  }

  private boolean refundPaymentProvider(Payment payment) {
    // 결제 제공자의 실제 환불 로직 구현
    return true; // 예시로 환불이 성공했다고 가정
  }

  private void updateSessionAvailability(Trainer trainer, int additionalSessions) {
    // 트레이너의 세션 예약 가능 상태를 업데이트하는 로직
    logger.info("트레이너 {}의 세션 예약 가능 상태가 업데이트되었습니다. 추가 세션 수: {}", trainer.getTrainerId(), additionalSessions);
  }

  private void sendNotification(User user, String message) {
    // 유저에게 알림을 보내는 로직 (예: 이메일, SMS 등)
    logger.info("알림 전송: {} - {}", user.getUsername(), message);
  }

  private void applyMembershipBenefits(User user) {
    // 유저의 멤버십 혜택을 적용하는 로직 (예: 할인율, 우선 예약 등)
    logger.info("유저 {}에게 멤버십 혜택이 적용되었습니다.", user.getUsername());
  }

  private void cancelMembershipReservations(User user) {
    // 유저의 멤버십과 관련된 모든 예약을 취소하는 로직
    logger.info("유저 {}의 멤버십 예약이 취소되었습니다.", user.getUsername());
  }
}