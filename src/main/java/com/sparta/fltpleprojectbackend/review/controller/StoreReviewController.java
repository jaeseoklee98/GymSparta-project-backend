package com.sparta.fltpleprojectbackend.review.controller;

import com.sparta.fltpleprojectbackend.common.CommonResponse;
import com.sparta.fltpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fltpleprojectbackend.review.dto.ReviewResponse;
import com.sparta.fltpleprojectbackend.review.service.StoreReviewService;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usermembership/{usermembershipId}/reviews")
@RequiredArgsConstructor
public class StoreReviewController {

    private final StoreReviewService storeReviewService;

    @PostMapping
    public ResponseEntity<CommonResponse<ReviewResponse>> createStoreReview(
            @PathVariable Long usermembershipId, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponse response = storeReviewService.createStoreReview(reviewRequest, usermembershipId, userDetails);
        CommonResponse<ReviewResponse> commonResponse = new CommonResponse<>(
                HttpStatus.CREATED.value(), "리뷰 등록 완료", response);
        return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<ReviewResponse>> updateStoreReview(
            @PathVariable Long usermembershipId, @PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponse response = storeReviewService.updateStoreReview(usermembershipId, reviewId, reviewRequest, userDetails);
        CommonResponse<ReviewResponse> commonResponse = new CommonResponse<>(
                HttpStatus.OK.value(), "리뷰 수정 완료", response);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    /*
    @GetMapping("/store/{storeId}")
    public ResponseEntity<CommonResponse<List<ReviewResponse>>> getAllStoreReviews(@PathVariable Long storeId) {
        List<ReviewResponse> responses = storeReviewService.getAllStoreReviews(storeId);
        CommonResponse<List<ReviewResponse>> commonResponse = new CommonResponse<>(
                HttpStatus.OK.value(), "매장 리뷰 조회 완료", responses);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }
     */

    @GetMapping
    public ResponseEntity<CommonResponse<List<ReviewResponse>>> getAllReviewsByMembershipId(@PathVariable Long usermembershipId) {
        List<ReviewResponse> responses = storeReviewService.getAllReviewsByMembershipId(usermembershipId);
        CommonResponse<List<ReviewResponse>> commonResponse = new CommonResponse<>(
                HttpStatus.OK.value(), "회원권 리뷰 조회 완료", responses);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<CommonResponse<List<ReviewResponse>>> getUserStoreReviews(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ReviewResponse> responses = storeReviewService.getUserStoreReviews(userDetails);
        CommonResponse<List<ReviewResponse>> commonResponse = new CommonResponse<>(
                HttpStatus.OK.value(), "유저 리뷰 조회 완료", responses);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }
}