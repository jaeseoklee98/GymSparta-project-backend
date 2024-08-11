package com.sparta.fitpleprojectbackend.review.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.payment.repository.PaymentRepository;
import com.sparta.fitpleprojectbackend.product.entity.Product;
import com.sparta.fitpleprojectbackend.product.repository.ProductRepository;
import com.sparta.fitpleprojectbackend.review.dto.ReportRequest;
import com.sparta.fitpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fitpleprojectbackend.review.dto.ReviewResponse;
import com.sparta.fitpleprojectbackend.review.entity.Review;
import com.sparta.fitpleprojectbackend.review.exception.ReviewException;
import com.sparta.fitpleprojectbackend.review.repository.ReviewRepository;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.store.entity.Store;
import com.sparta.fitpleprojectbackend.store.repository.StoreRepository;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import com.sparta.fitpleprojectbackend.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final StoreRepository storeRepository;
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;
  private final PaymentRepository paymentRepository;
  private final ProductRepository productRepository;

  /**
   * 리뷰 생성
   *
   * @param reviewRequest 리뷰 요청 DTO
   * @param userDetails 현재 인증된 사용자 정보
   * @return 생성된 리뷰의 응답 DTO
   * @throws ReviewException 유저, 상점, 트레이너, 회원권, PT 세션, 결제 정보가 없을 경우 발생
   */
  @Transactional
  public ReviewResponse createReview(ReviewRequest reviewRequest, UserDetailsImpl userDetails) {
    User user = userRepository.findById(userDetails.getUser().getId())
        .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_USER));

    Store store = null;
    Trainer trainer = null;
    Product product = null;

    if (reviewRequest.getStoreId() != null) {
      store = storeRepository.findById(reviewRequest.getStoreId())
          .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_STORE));
    } else if (reviewRequest.getTrainerId() != null) {
      trainer = trainerRepository.findById(reviewRequest.getTrainerId())
          .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_TRAINER));
    }

    if (reviewRequest.getProductId() != null) {
      product = productRepository.findById(reviewRequest.getProductId())
          .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_PRODUCT));
    }

    Payment payment = paymentRepository.findById(reviewRequest.getPaymentId())
        .orElseThrow(() -> new ReviewException(ErrorType.PAYMENT_NOT_FOUND));

    Review review = new Review(user, store, trainer, payment, product, reviewRequest.getRating(), reviewRequest.getComment(), reviewRequest.getReviewType());
    reviewRepository.save(review);

    return new ReviewResponse(review);
  }

  /**
   * 리뷰 수정
   *
   * @param reviewId 수정할 리뷰의 ID
   * @param reviewRequest 수정 요청 DTO
   * @param userDetails 현재 인증된 사용자 정보
   * @return 수정된 리뷰의 응답 DTO
   * @throws ReviewException 리뷰가 없거나 수정 권한이 없을 경우 발생
   */
  @Transactional
  public ReviewResponse updateReview(Long reviewId, ReviewRequest reviewRequest, UserDetailsImpl userDetails) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new ReviewException(ErrorType.REVIEW_NOT_FOUND));

    if (!review.getUser().getId().equals(userDetails.getUser().getId())) {
      throw new ReviewException(ErrorType.USER_NOT_AUTHORIZED);
    }

    if (review.getCreatedAt().isBefore(LocalDateTime.now().minusDays(7))) {
      throw new ReviewException(ErrorType.REVIEW_MODIFICATION_PERIOD_EXPIRED);
    }

    review.update(reviewRequest.getRating(), reviewRequest.getComment());
    return new ReviewResponse(review);
  }

  /**
   * 유저가 작성한 모든 리뷰 조회
   *
   * @param userDetails 현재 인증된 사용자 정보
   * @return 유저가 작성한 리뷰 리스트
   */
  @Transactional(readOnly = true)
  public List<ReviewResponse> getUserReviews(UserDetailsImpl userDetails) {
    return reviewRepository.findByUserId(userDetails.getUser().getId()).stream()
        .map(ReviewResponse::new)
        .collect(Collectors.toList());
  }

  /**
   * 점주가 신고된 리뷰 조회
   *
   * @param userDetails 현재 인증된 사용자 정보
   * @return 점주가 소유한 가게에 대한 신고된 리뷰 리스트
   * @throws ReviewException 매장 정보가 없을 경우 발생
   */
  @Transactional(readOnly = true)
  public List<ReviewResponse> getReportedReviewsForOwner(UserDetailsImpl userDetails) {
    Store store = storeRepository.findAllByOwnerAccountId(userDetails.getUser().getAccountId()).stream()
        .findFirst()
        .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_STORE));

    return reviewRepository.findReportedReviewsByStoreId(store.getId()).stream()
        .map(ReviewResponse::new)
        .collect(Collectors.toList());
  }

  /**
   * 신고된 리뷰 삭제
   *
   * @param reviewId 삭제할 리뷰의 ID
   * @param userDetails 현재 인증된 사용자 정보
   * @throws ReviewException 리뷰가 없거나 삭제 권한이 없을 경우 발생
   */
  @Transactional
  public void deleteReportedReview(Long reviewId, UserDetailsImpl userDetails) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new ReviewException(ErrorType.REVIEW_NOT_FOUND));

    Store store = storeRepository.findAllByOwnerAccountId(userDetails.getUser().getAccountId()).stream()
        .findFirst()
        .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_STORE));

    if (review.getStore() == null || !review.getStore().getId().equals(store.getId())) {
      throw new ReviewException(ErrorType.USER_NOT_AUTHORIZED);
    }

    reviewRepository.delete(review);
  }

  /**
   * 리뷰 신고
   *
   * @param reviewId 신고할 리뷰의 ID
   * @param reportRequest 신고 요청 DTO
   * @throws ReviewException 리뷰가 없을 경우 발생
   */
  @Transactional
  public void reportReview(Long reviewId, ReportRequest reportRequest) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new ReviewException(ErrorType.REVIEW_NOT_FOUND));
    reviewRepository.save(review);
    // 알림 시스템을 통한 리뷰 신고 알림 (예시)
    // notificationService.sendReviewNotification("리뷰가 신고되었습니다.", review.getUser().getId());
  }
}