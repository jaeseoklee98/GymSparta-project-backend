package com.sparta.fltpleprojectbackend.review.controller;

import com.sparta.fltpleprojectbackend.common.CommonResponse;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fltpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fltpleprojectbackend.review.dto.ReviewResponse;
import com.sparta.fltpleprojectbackend.review.service.TrainerReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TrainerReviewController {

    private final TrainerReviewService trainerReviewService;

    @PostMapping("/userpt/{userPtId}/reviews")
    public ResponseEntity<CommonResponse<ReviewResponse>> createTrainerReview(
            @PathVariable Long userPtId, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponse response = trainerReviewService.createTrainerReview(reviewRequest, userPtId, userDetails);
        CommonResponse<ReviewResponse> commonResponse = new CommonResponse<>(
                HttpStatus.CREATED.value(), "리뷰 등록 완료", response);
        return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
    }

    @PutMapping("/userpt/{userPtId}/reviews/{reviewId}")
    public ResponseEntity<CommonResponse<ReviewResponse>> updateTrainerReview(
            @PathVariable Long userPtId, @PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponse response = trainerReviewService.updateTrainerReview(userPtId, reviewId, reviewRequest, userDetails);
        CommonResponse<ReviewResponse> commonResponse = new CommonResponse<>(
                HttpStatus.OK.value(), "리뷰 수정 완료", response);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @GetMapping("/userpt/{userPtId}/reviews/user")
    public ResponseEntity<CommonResponse<List<ReviewResponse>>> getUserTrainerReviews(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ReviewResponse> responses = trainerReviewService.getUserTrainerReviews(userDetails);
        CommonResponse<List<ReviewResponse>> commonResponse = new CommonResponse<>(
                HttpStatus.OK.value(), "유저 리뷰 조회 완료", responses);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @GetMapping("/userpt/{userPtId}/reviews/trainer")
    public ResponseEntity<CommonResponse<List<ReviewResponse>>> getTrainerReviewsByUserPtId(@PathVariable Long userPtId) {
        List<ReviewResponse> responses = trainerReviewService.getTrainerReviewsByUserPtId(userPtId);
        CommonResponse<List<ReviewResponse>> commonResponse = new CommonResponse<>(
                HttpStatus.OK.value(), "트레이너 리뷰 조회 완료", responses);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }
}