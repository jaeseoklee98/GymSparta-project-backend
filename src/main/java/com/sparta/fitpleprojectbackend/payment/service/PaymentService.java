package com.sparta.fitpleprojectbackend.payment.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.payment.dto.PaymentRequest;
import com.sparta.fitpleprojectbackend.payment.entity.Payment;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentStatus;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentType;
import com.sparta.fitpleprojectbackend.payment.enums.PtTimes;
import com.sparta.fitpleprojectbackend.payment.repository.PaymentRepository;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
  private final PaymentRepository paymentRepository;
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;

  public PaymentService(PaymentRepository paymentRepository, TrainerRepository trainerRepository, UserRepository userRepository) {
    this.paymentRepository = paymentRepository;
    this.trainerRepository = trainerRepository;
    this.userRepository = userRepository;
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

    Payment payment = new Payment(
        trainer,
        user,
        null,
        ptTimes,
        request.getPaymentType(),
        request.getAmount(),
        PaymentStatus.PENDING,
        LocalDateTime.now(),
        LocalDateTime.now().plusDays(ptTimes.getTimes() / 30),
        request.isMembership()
    );

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

    switch (paymentType) {
      case CREDIT_CARD:
        break;
      case DEBIT_CARD:
        break;
      case CASH:
        break;
      default:
        throw new IllegalArgumentException("지원하지 않는 결제 수단입니다: " + paymentType);
    }

    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));

    payment.setPaymentStatus(PaymentStatus.APPROVED);
    payment = new Payment(
        payment.getTrainer(),
        payment.getUser(),
        payment.getProduct(),
        payment.getPtTimes(),
        paymentType,
        payment.getAmount(),
        PaymentStatus.APPROVED,
        payment.getPaymentDate(),
        payment.getExpiryDate(),
        payment.isMembership()
    );

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
      paymentRepository.save(payment);
    } else {
      throw new CustomException(ErrorType.REFUND_PROCESSING_ERROR);
    }
  }

  private boolean refundPaymentProvider(Payment payment) {
    // 결제 제공자의 실제 환불 로직 구현
    return true;
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
}