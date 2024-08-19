package com.sparta.gymspartaprojectbackend.review.controller;

import com.sparta.gymspartaprojectbackend.common.CommonResponse;
import com.sparta.gymspartaprojectbackend.review.dto.ReportRequest;
import com.sparta.gymspartaprojectbackend.review.dto.ReviewRequest;
import com.sparta.gymspartaprojectbackend.review.dto.ReviewResponse;
import com.sparta.gymspartaprojectbackend.review.service.ReviewService;
import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  /**
   * 리뷰 생성
   *
   * @param reviewRequest 리뷰 요청 DTO
   * @param userDetails 현재 인증된 사용자 정보
   * @return 생성된 리뷰의 응답 DTO
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping
  public ResponseEntity<CommonResponse<ReviewResponse>> createReview(
      @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReviewResponse response = reviewService.createReview(reviewRequest, userDetails);
    CommonResponse<ReviewResponse> commonResponse = new CommonResponse<>(
        HttpStatus.CREATED.value(), "리뷰 등록 완료", response);
    return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
  }

  /**
   * 리뷰 수정
   *
   * @param reviewId 수정할 리뷰의 ID
   * @param reviewRequest 리뷰 수정 요청 DTO
   * @param userDetails 현재 인증된 사용자 정보
   * @return 수정된 리뷰의 응답 DTO
   */
  @PutMapping("/{reviewId}")
  public ResponseEntity<CommonResponse<ReviewResponse>> updateReview(
      @PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReviewResponse response = reviewService.updateReview(reviewId, reviewRequest, userDetails);
    CommonResponse<ReviewResponse> commonResponse = new CommonResponse<>(
        HttpStatus.OK.value(), "리뷰 수정 완료", response);
    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }

  /**
   * 유저가 작성한 리뷰 조회
   *
   * @param userDetails 현재 인증된 사용자 정보
   * @return 유저가 작성한 리뷰 리스트
   */
  @GetMapping("/user")
  public ResponseEntity<CommonResponse<List<ReviewResponse>>> getUserReviews(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<ReviewResponse> responses = reviewService.getUserReviews(userDetails);
    CommonResponse<List<ReviewResponse>> commonResponse = new CommonResponse<>(
        HttpStatus.OK.value(), "유저 리뷰 조회 완료", responses);
    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }

  /**
   * 신고된 리뷰 조회
   *
   * @param userDetails 현재 인증된 사용자 정보
   * @return 신고된 리뷰 리스트
   */
  @GetMapping("/reported")
  public ResponseEntity<CommonResponse<List<ReviewResponse>>> getReportedReviews(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<ReviewResponse> responses = reviewService.getReportedReviewsForOwner(userDetails);
    CommonResponse<List<ReviewResponse>> commonResponse = new CommonResponse<>(
        HttpStatus.OK.value(), "신고된 리뷰 조회 완료", responses);
    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }

  /**
   * 매장별 평균 리뷰 평점 조회
   *
   * @param storeId 조회할 매장의 ID
   * @return 매장별 평균 리뷰 평점
   */
  @GetMapping("/store/{storeId}/average-rating")
  public ResponseEntity<CommonResponse<Double>> getAverageRatingByStoreId(@PathVariable Long storeId) {
    Double averageRating = reviewService.getAverageRatingByStoreId(storeId);
    CommonResponse<Double> commonResponse = new CommonResponse<>(
            HttpStatus.OK.value(), "매장별 평균 리뷰 평점 조회 완료", averageRating);
    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }

  /**
   * 트레이너별 평균 리뷰 평점 조회
   *
   * @param trainerId 조회할 트레이너의 ID
   * @return 트레이너별 평균 리뷰 평점
   */
  @GetMapping("/trainer/{trainerId}/average-rating")
  public ResponseEntity<CommonResponse<Double>> getAverageRatingByTrainerId(@PathVariable Long trainerId) {
    Double averageRating = reviewService.getAverageRatingByTrainerId(trainerId);
    CommonResponse<Double> commonResponse = new CommonResponse<>(
            HttpStatus.OK.value(), "트레이너별 평균 리뷰 평점 조회 완료", averageRating);
    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }

  /**
   * 매장별 리뷰 조회
   *
   * @param storeId 조회할 매장의 ID
   * @return 매장별 리뷰 리스트
   */
  @GetMapping("/store/{storeId}/reviews")
  public ResponseEntity<CommonResponse<List<ReviewResponse>>> getReviewsByStoreId(@PathVariable Long storeId) {
    List<ReviewResponse> reviews = reviewService.getReviewsByStoreId(storeId);
    CommonResponse<List<ReviewResponse>> commonResponse = new CommonResponse<>(
            HttpStatus.OK.value(), "매장 리뷰 조회 완료", reviews);
    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }

    /**
   * 트레이너별 리뷰 조회
   *
   * @param trainerId 조회할 트레이너의 ID
   * @return 트레이너별 리뷰 리스트
     */
  @GetMapping("/trainer/{trainerId}/reviews")
  public ResponseEntity<CommonResponse<List<ReviewResponse>>> getReviewsByTrainerId(@PathVariable Long trainerId) {
    List<ReviewResponse> reviews = reviewService.getReviewsByTrainerId(trainerId);
    CommonResponse<List<ReviewResponse>> commonResponse = new CommonResponse<>(
            HttpStatus.OK.value(), "트레이너 리뷰 조회 완료", reviews);
    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }

  /**
   * 신고된 리뷰 삭제
   *
   * @param reviewId 삭제할 리뷰의 ID
   * @param userDetails 현재 인증된 사용자 정보
   * @return 리뷰 삭제 결과 메시지
   */
  @DeleteMapping("/{reviewId}")
  public ResponseEntity<CommonResponse<String>> deleteReportedReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    reviewService.deleteReportedReview(reviewId, userDetails);
    CommonResponse<String> commonResponse = new CommonResponse<>(
        HttpStatus.OK.value(), "리뷰 삭제 완료", "신고된 리뷰가 삭제되었습니다.");
    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }

  /**
   * 리뷰 신고
   *
   * @param reviewId 신고할 리뷰의 ID
   * @param reportRequest 리뷰 신고 요청 DTO
   * @return 리뷰 신고 결과 메시지
   */
  @PostMapping("/{reviewId}/report")
  public ResponseEntity<CommonResponse<String>> reportReview(@PathVariable Long reviewId, @RequestBody ReportRequest reportRequest) {
    reviewService.reportReview(reviewId, reportRequest);
    CommonResponse<String> commonResponse = new CommonResponse<>(
        HttpStatus.OK.value(), "리뷰 신고 완료", "리뷰가 신고되었습니다.");
    return new ResponseEntity<>(commonResponse, HttpStatus.OK);
  }
}