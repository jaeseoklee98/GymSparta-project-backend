package com.sparta.fitpleprojectbackend.ptsesson.controller;


import com.sparta.fitpleprojectbackend.ptsesson.dto.PtInformationRequest;
import com.sparta.fitpleprojectbackend.ptsesson.entity.PtInfomation;
import com.sparta.fitpleprojectbackend.ptsesson.repository.PtInformationRepository;
import com.sparta.fitpleprojectbackend.ptsesson.service.PtPaymentService;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ptpayments")
public class PtPaymentController {

  private PtPaymentService ptPaymentService;
  private final PtInformationRepository ptInformationRepository;
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;

  public PtPaymentController(PtPaymentService ptPaymentService,
      PtInformationRepository ptInformationRepository,
      TrainerRepository trainerRepository,
      UserRepository userRepository) {
    this.ptPaymentService = ptPaymentService;
    this.ptInformationRepository = ptInformationRepository;
    this.trainerRepository = trainerRepository;
    this.userRepository = userRepository;
  }

  /**
   * 결제 요청 처리 및 결제 페이지 반환
   *
   * @param request 결제 요청 정보
   * @return 결제 정보 출력 메시지
   */
  @PostMapping("/process")
  @Transactional
  public ResponseEntity<String> processPayment(@RequestBody PtInformationRequest request) {
    ptPaymentService.validateTrainerAndUser(request.getTrainerId(), request.getUserId());

    ptPaymentService.checkDuplicatePts(request.getTrainerId(), request.getUserId());

    PtInfomation ptInformation = ptPaymentService.savePtInformation(request);

    String completeMessage = ptPaymentService.showPaymentPage(ptInformation);

    return ResponseEntity.ok(completeMessage);
  }

  }


