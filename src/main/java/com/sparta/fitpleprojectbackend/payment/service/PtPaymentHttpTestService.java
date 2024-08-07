package com.sparta.fitpleprojectbackend.payment.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.payment.dto.PtInformationRequest;
import com.sparta.fitpleprojectbackend.payment.dto.PtPaymentRequest;
import com.sparta.fitpleprojectbackend.payment.dto.PtPaymentValidateRequest;
import com.sparta.fitpleprojectbackend.payment.entity.PtInfomation;
import com.sparta.fitpleprojectbackend.payment.entity.PtPayment;
import com.sparta.fitpleprojectbackend.payment.entity.UserPt;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentStatus;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentType;
import com.sparta.fitpleprojectbackend.payment.enums.PtTimes;
import com.sparta.fitpleprojectbackend.payment.repository.PtInformationRepository;
import com.sparta.fitpleprojectbackend.payment.repository.PtPaymentRepository;
import com.sparta.fitpleprojectbackend.payment.repository.UserPtRepository;
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
public class PtPaymentHttpTestService {

  private static final int MAX_RETRY_ATTEMPTS = 5;
  private static final String TEST = "testId"; // 테스트용
  private static final Logger logger = LoggerFactory.getLogger(PtPaymentService.class);
  private final PtPaymentRepository ptPaymentRepository;
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;
  private final UserPtRepository userPtRepository;
  private final PtInformationRepository ptInformationRepository;

  public PtPaymentHttpTestService(PtPaymentRepository ptPaymentRepository,
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
        .orElseThrow(() -> {
          logger.error("트레이너 ID 없음: ", trainerId);
          return new CustomException(ErrorType.TRAINER_NOT_FOUND);
        });

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          logger.error("유저 ID 없음: ", userId);
          return new CustomException(ErrorType.USER_NOT_FOUND);
        });

    return new PtPaymentValidateRequest(trainer, user);
  }

  /**
   * 결제 요청 정보 임시 저장 [클라이언트 구입 요청 ~ 결제창 반환]
   *
   * @param request 결제 요청 정보
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
      logger.error("중복 결제: 트레이너 ID, 유저 ID", trainerId, userId);
      throw new CustomException(ErrorType.RESERVATION_CONFLICT);
    }
  }

  /**
   * 결제 페이지 조회 [클라이언트 구입 요청 ~ 결제창 반환]
   *
   * @param ptInformation 결제 정보
   * @return 결제 정보 출력 메세지
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
      logger.info("결제 요청 전 정보 저장 시작: 사용자 ID = , 요청 정보 = ", userId, request);

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

      logger.info("결제 정보 임시 저장 완료: ", savedPayment);

      return savedPayment;

    } catch (CustomException e) {

      throw e;
    } catch (Exception e) {

      throw new CustomException(ErrorType.PAYMENT_FAILED);
    }
  }

  /**
   * 결제 수단 선택 (api 연결)
   */
  public void selectPaymentMethod(PaymentType paymentType) {
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
  }
//  /**
//   * 결제 정보 검증 및 상태 업데이트
//   *
//   * @param ptpaymentId 결제 ID
//   * @return 승인된 결제 정보
//   */
//  @Transactional
//  public PtPayment approvePayment(Long ptpaymentId) {
//
//    PtPayment ptPayment = ptPaymentRepository.findById(ptpaymentId)
//        .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));
//
//    // API 결제 데이터 테스트용
//    PtPayment apiPayment = new PtPayment(
//        new Trainer(),
//        new User(),
//        PtTimes.SIXTY_TIMES,
//        PaymentType.UNDEFINED,
//        100.0,
//        PaymentStatus.PENDING,
//        LocalDateTime.now().minusDays(1),
//        LocalDateTime.now().plusDays(30),
//        true
//    );
//
//    if (!ptPayment.getTrainer().equals(apiPayment.getTrainer()) ||
//        !ptPayment.getUser().equals(apiPayment.getUser()) ||
//        !ptPayment.getPtTimes().equals(apiPayment.getPtTimes()) ||
//        ptPayment.getAmount() != apiPayment.getAmount() ||
//        ptPayment.isMembership() != apiPayment.isMembership()) {
//      throw new CustomException(ErrorType.PAYMENT_MISMATCH);
//    }
//
//    return savePayment(ptPayment);
//  }

//  /**
//   * 결제 상태를 APPROVED 변경
//   *
//   * @param ptPayment 결제 정보
//   * @return 승인된 결제 정보
//   */
//  public PtPayment savePayment(PtPayment ptPayment) {
//    PtPayment savedPayment = new PtPayment(
//        ptPayment.getTrainer(),
//        ptPayment.getUser(),
//        ptPayment.getPtTimes(),
//        ptPayment.getPaymentType(),
//        ptPayment.getAmount(),
//        PaymentStatus.APPROVED,
//        ptPayment.getPaymentDate(),
//        ptPayment.getExpiryDate(),
//        ptPayment.isMembership()
//    );
//
//    return ptPaymentRepository.save(savedPayment);
//  }

  /**
   * 결제를 승인함 (toss 예정)
   *
   * @param userId 유저 ID
   * @param amount 결제 금액
   * @return 결제 승인 여부
   */
  public boolean approvePayment(Long userId, double amount) {
    logger.info("결제 승인 완료: {}", amount);
    return true;
  }

  /**
   * 결제를 완료 최종 결제 정보를 저장
   *
   * @param request 결제 요청 정보
   * @param userId  유저 ID
   * @return 저장된 결제 정보
   * @throws CustomException 결제 처리 중 오류 발생 시
   */
  @Transactional
  public PtPayment completePayment(PtPaymentRequest request, Long userId) {
    try {
      logger.info("결제 완료 시작: 사용자 ID = , 요청 정보 = ", userId, request);

      validateTrainerAndUser(request.getTrainerId(), userId);
      checkDuplicatePt(request.getTrainerId(), userId);

      Trainer trainer = trainerRepository.findById(request.getTrainerId())
          .orElseThrow(() -> new CustomException(ErrorType.TRAINER_NOT_FOUND));

      double totalAmount = 600;

      boolean paymentApproved = false;
      int attempt = 0;

      while (attempt < MAX_RETRY_ATTEMPTS && !paymentApproved) {
        try {
          paymentApproved = approvePayment(userId, totalAmount);
        } catch (Exception e) {
          attempt++;
          logger.error("실패 재시도 중... 시도 횟수: ", attempt, e);

          if (attempt >= MAX_RETRY_ATTEMPTS) {

            throw new CustomException(ErrorType.PAYMENT_FAILED);
          }
        }
      }

      if (!paymentApproved) {
        throw new CustomException(ErrorType.PAYMENT_APPROVAL_FAILED);
      }

      PtPayment ptPayment = new PtPayment(
          trainer,
          userRepository.findById(userId)
              .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND)),
          request.getPtTimes(),
          request.getPaymentType(),
          totalAmount,
          PaymentStatus.COMPLETED,
          LocalDateTime.now(),
          LocalDateTime.now().plusDays(request.getPtTimes().getTimes() / 30),
          request.isMembership()
      );

      PtPayment savedPayment = ptPaymentRepository.save(ptPayment);

      logger.info("결제 완료 및 저장 완료: ", savedPayment);

      return savedPayment;
    } catch (Exception e) {

      throw new CustomException(ErrorType.PAYMENT_FAILED);
    }
  }

//  /**
//   * 결제 정보의 일관성을 검증
//   *
//   * @param ptPayment 결제 정보
//   * @throws CustomException 결제 정보가 일치하지 않는 경우
//   */
//  public void verifyPayment(PtPayment ptPayment) {
//    PtPayment existingPayment = ptPaymentRepository.findById(ptPayment.getId())
//        .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));
//
//    if (!existingPayment.equals(ptPayment)) {
//      throw new CustomException(ErrorType.PAYMENT_MISMATCH);
//    }
//  }

  /**
   * 결제 정보를 UserPt에 저장
   *
   * @param ptPayment 결제 정보
   */
  public void savePaymentToUserPt(PtPayment ptPayment) {
    UserPt userPt = new UserPt(
        ptPayment.getTrainer(),
        ptPayment.getUser(),
        ptPayment.getPtTimes(),
        ptPayment.getPaymentType(),
        ptPayment.getAmount(),
        ptPayment.getPaymentStatus(),
        ptPayment.getPaymentDate(),
        ptPayment.getExpiryDate(),
        ptPayment.isMembership(),
        true
    );
    userPtRepository.save(userPt);
  }

  /**
   * 결제 완료 페이지
   *
   * @param ptPayment 결제 정보
   * @return 결제 완료 메시지
   */
  public String PaymentCompletePage(PtPayment ptPayment) {
    return String.format(
        "결제가 성공적으로 완료되었습니다!\n" +
            "트레이너: %s\n" +
            "유저: %s\n" +
            "PT 횟수: %d\n" +
            "금액: %.2f\n" +
            "결제 일자: %s\n" +
            "만료 일자: %s\n" +
            "회원권: %b",
        ptPayment.getTrainer().getTrainerName(),
        ptPayment.getUser().getUserName(),
        ptPayment.getPtTimes().getTimes(),
        ptPayment.getAmount(),
        ptPayment.getPaymentDate(),
        ptPayment.getExpiryDate(),
        ptPayment.isMembership()
    );
  }
}



