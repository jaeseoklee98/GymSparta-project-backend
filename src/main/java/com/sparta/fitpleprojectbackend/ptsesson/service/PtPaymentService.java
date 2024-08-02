package com.sparta.fitpleprojectbackend.ptsesson.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtInformationRequest;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtPaymentRequest;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtPaymentValidateRequest;
import com.sparta.fitpleprojectbackend.ptsesson.entity.PtInfomation;
import com.sparta.fitpleprojectbackend.ptsesson.entity.PtPayment;
import com.sparta.fitpleprojectbackend.ptsesson.entity.UserPt;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PaymentStatus;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PaymentType;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PtTimes;
import com.sparta.fitpleprojectbackend.ptsesson.repository.PtInformationRepository;
import com.sparta.fitpleprojectbackend.ptsesson.repository.PtPaymentRepository;
import com.sparta.fitpleprojectbackend.ptsesson.repository.UserPtRepository;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PtPaymentService {

  private static final Logger logger = LoggerFactory.getLogger(PtPaymentService.class);
  private final PtPaymentRepository ptPaymentRepository;
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;
  private final UserPtRepository userPtRepository;
  private final PtInformationRepository ptInformationRepository;

  public PtPaymentService(PtPaymentRepository ptPaymentRepository,
      TrainerRepository trainerRepository,
      UserRepository userRepository, UserPtRepository userPtRepository,
      PtInformationRepository ptInformationRepository) {
    this.ptPaymentRepository = ptPaymentRepository;
    this.trainerRepository = trainerRepository;
    this.userRepository = userRepository;
    this.userPtRepository = userPtRepository;
    this.ptInformationRepository = ptInformationRepository;
  }

  /**
   * 트레이너와 유저 검증 [검증용]
   *
   * @param trainerId 트레이너 ID
   * @param userId    유저 ID
   * @throws CustomException 트레이너 또는 유저가 존재하지 않는 경우
   */
  public PtPaymentValidateRequest validateTrainerAndUser(Long trainerId, Long userId) {
    Trainer trainer = trainerRepository.findById(trainerId)
        .orElseThrow(() -> new CustomException(ErrorType.TRAINER_NOT_FOUND));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

    return new PtPaymentValidateRequest(trainer, user);
  }

  /**
   * 결제 요청 정보 임시 저장 [클라이언트 구입 요청 ~ 결제창 반환]
   *
   * @param request 결제 요청 정보
   * @return 저장된 결제 정보
   */
  @Transactional
  public PtInfomation savePtInformation(PtInformationRequest request) {
    PtPaymentValidateRequest validateRequest = validateTrainerAndUser(request.getTrainerId(),
        request.getUserId());

    Trainer trainer = validateRequest.getTrainer();
    User user = validateRequest.getUser();

    PtInfomation ptInformation = new PtInfomation(
        trainer,
        user,
        trainer.getPtPrice(),
        trainer.isMembership(),
        request.getPtTimes() != null ? request.getPtTimes() : PtTimes.TEN_TIMES,
        PaymentStatus.PENDING
    );

    return ptInformationRepository.save(ptInformation);
  }


  /**
   * 중복 PT 예약을 체크 [클라이언트 구입 요청 ~ 결제창 반환]
   *
   * @param trainerId 트레이너 ID
   * @param userId    유저 ID
   * @throws CustomException 중복 결제 기록이 있는 경우
   */
  public void checkDuplicatePt(Long trainerId, Long userId) {
    List<UserPt> existingPayments = userPtRepository.findAllByTrainerIdAndUserIdAndIsActive(
        trainerId, userId, true);

    if (!existingPayments.isEmpty()) {
      throw new CustomException(ErrorType.RESERVATION_CONFLICT);
    }
  }

  /**
   * 결제 페이지 조회 [클라이언트 구입 요청 ~ 결제창 반환]
   *
   * @param ptInformation 결제 정보
   * @return 결제 정보 출력 메시지
   */
  public String showPaymentPage(PtInfomation ptInformation) {
    String completeMessage = String.format(
        "결제 화면\n" +
            "트레이너: %s\n" +
            "유저: %s\n" +
            "PT 횟수: %d\n" +
            "금액: %.2f\n" +
            "회원권: %b",
        ptInformation.getTrainer().getTrainerName(),
        ptInformation.getUser().getUserName(),
        ptInformation.getPtTimes().getTimes(),
        ptInformation.getPtPrice(),
        ptInformation.isMembership()
    );

    return completeMessage;
  }

  /**
   * 사용자가 PT 횟수를 선택 [횟수 선택과 총액 계산 ~ API 결제]
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
   * API 요청 전 결제 정보를 저장 [횟수 선택과 총액 계산 ~ API 결제]
   *
   * @param request 결제 요청 정보
   * @param userId  유저 ID
   * @param amount  프론트에서 계산한 총액
   * @return 임시 저장된 결제 정보
   * @throws CustomException 결제 정보 저장 중 오류 발생
   */
  @Transactional
  public PtPayment SavePayment(PtPaymentRequest request, Long userId, double amount) {
    try {

      if (amount <= 0) {
        throw new CustomException(ErrorType.INVALID_INPUT);
      }

      PtPaymentValidateRequest validateRequest = validateTrainerAndUser(request.getTrainerId(),
          userId);
      Trainer trainer = validateRequest.getTrainer();
      User user = validateRequest.getUser();
      PtTimes ptTimes = selectPtTimes(request.getPtTimes().name());

      PtPayment ptPayment = new PtPayment(
          trainer,
          user,
          ptTimes,
          PaymentType.UNDEFINED,
          amount,
          PaymentStatus.PENDING,
          LocalDateTime.now(),
          LocalDateTime.now().plusDays(ptTimes.getTimes() / 30),
          request.isMembership()
      );

      PtPayment savedPayment = ptPaymentRepository.save(ptPayment);

      return savedPayment;

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      throw new CustomException(ErrorType.PAYMENT_FAILED);
    }
  }

  /**
   * 결제 승인 및 상태 업데이트
   *
   * @param ptPaymentId 결제 ID
   * @param paymentType 결제 수단
   * @return 승인된 결제 정보
   */
  @Transactional
  public PtPayment approvePayment(Long ptPaymentId, PaymentType paymentType) {

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

    PtPayment ptPayment = ptPaymentRepository.findById(ptPaymentId)
        .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));

    PtPayment approvedPayment = new PtPayment(
        ptPayment.getTrainer(),
        ptPayment.getUser(),
        ptPayment.getPtTimes(),
        ptPayment.getPaymentType(),
        ptPayment.getAmount(),
        PaymentStatus.APPROVED,
        ptPayment.getPaymentDate(),
        ptPayment.getExpiryDate(),
        ptPayment.isMembership()
    );

    return ptPaymentRepository.save(approvedPayment);
  }
}


