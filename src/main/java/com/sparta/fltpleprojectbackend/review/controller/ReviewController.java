package com.sparta.fltpleprojectbackend.review.controller;

import com.sparta.fltpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fltpleprojectbackend.review.dto.ReviewResponse;
import com.sparta.fltpleprojectbackend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/users/{storeId}/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping()
    public ReviewResponse createReview(@RequestBody ReviewRequest reviewRequest, @PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.createReview(reviewRequest, storeId, userDetails.getUserId());
    }

    @PutMapping("/{reviewId}")
    public ReviewResponse updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.updateReview(reviewId, reviewRequest, userDetails.getUserId());
    }
}
