package com.sparta.fitpleprojectbackend.ptsesson.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtInformationRequest;
import com.sparta.fitpleprojectbackend.ptsesson.entity.PtInfomation;
import com.sparta.fitpleprojectbackend.ptsesson.entity.UserPt;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PaymentStatus;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PtTimes;
import com.sparta.fitpleprojectbackend.ptsesson.repository.PtInformationRepository;
import com.sparta.fitpleprojectbackend.ptsesson.repository.PtPaymentRepository;
import com.sparta.fitpleprojectbackend.ptsesson.repository.UserPtRepository;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PtPaymentHttpTestService {
  private static final Logger logger = LoggerFactory.getLogger(PtPaymentService.class);
  private final PtPaymentRepository ptPaymentRepository;
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;
  private final UserPtRepository userPtRepository;
  private PtInformationRepository ptInformationRepository;


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
   * 결제 요청 정보 임시 저장
   *
   * @param request 결제 요청 정보
   */
  @Transactional
  public void savePtInformation(PtInformationRequest request) {
    Trainer trainer = trainerRepository.findById(request.getTrainerId())
        .orElseThrow(() -> new CustomException(ErrorType.TRAINER_NOT_FOUND));
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

    PtInfomation ptInformation = new PtInfomation(
        trainer,
        user,
        trainer.getPtPrice(),
        trainer.isMembership(),
        request.getPtTimes() != null ? request.getPtTimes() : PtTimes.TEN_TIMES,
        PaymentStatus.PENDING
    );
    ptInformationRepository.save(ptInformation);
  }

  /**
   * 트레이너와 유저 검증
   *
   * @param trainerId 트레이너 ID
   * @param userId    유저 ID
   * @throws CustomException 트레이너 또는 유저가 존재하지 않는 경우
   */
  public void validateTrainerAndUser(Long trainerId, Long userId) {
    boolean trainerExists = trainerRepository.existsById(trainerId);
    boolean userExists = userRepository.existsById(userId);

    if (!trainerExists && !userExists) {
      throw new CustomException(ErrorType.TRAINER_AND_USER_NOT_FOUND);
    } else if (!trainerExists) {
      throw new CustomException(ErrorType.TRAINER_NOT_FOUND);
    } else if (!userExists) {
      throw new CustomException(ErrorType.USER_NOT_FOUND);
    }
  }

  /**
   * 중복 결제를 방지하기 위해 최근 30일 이내에 결제 기록이 있는지 확인
   *
   * @param trainerId   트레이너 ID
   * @param userId      유저 ID
   * @throws CustomException 중복 결제 기록이 있는 경우
   */
  public void checkDuplicatePts(Long trainerId, Long userId) {
    List<UserPt> existingPayments = userPtRepository.findAllByTrainerIdAndUserIdAndIsActive(
        trainerId, userId, true);
    if (!existingPayments.isEmpty()) {
      throw new CustomException(ErrorType.RESERVATION_CONFLICT);
    }
  }

  /**
   * 결제 페이지에서 결제 정보 출력
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
}
