package com.sparta.fltpleprojectbackend.review.controller;

import com.sparta.fltpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fltpleprojectbackend.review.dto.ReviewResponse;
import com.sparta.fltpleprojectbackend.review.service.ReviewService;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/users/{storeId}/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    /*
    * 리뷰 생성
    * 매장 이용한 사용자만 리뷰 작성 가능
     */
    @PostMapping()
    public ReviewResponse createReview(@RequestBody ReviewRequest reviewRequest, @PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.createReview(reviewRequest, storeId, userDetails.getUser());
    }

    /*
     * 리뷰 수정
     * 리뷰 작성한 사용자만 리뷰 수정 가능
     */
    @PutMapping("/{reviewId}")
    public ReviewResponse updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.updateReview(reviewId, reviewRequest, userDetails.getUser());
    }

    /*
     * 리뷰 삭제
     * 리뷰 작성한 사용자만 리뷰 삭제 가능
     * 관리자 권한으로 부적절한 리뷰 삭제는 별도의 기능으로 구현
     */
    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUser());
    }

}
