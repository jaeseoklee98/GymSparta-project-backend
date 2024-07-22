package com.sparta.fltpleprojectbackend.review.service;

import com.sparta.fltpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fltpleprojectbackend.review.dto.ReviewResponse;
import com.sparta.fltpleprojectbackend.review.entity.Review;
import com.sparta.fltpleprojectbackend.review.repository.ReviewRepository;
import com.sparta.fltpleprojectbackend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewResponse createReview(ReviewRequest reviewRequest, Long storeId, User user) {
        // 주문에서 상품 구매 내역에 따라서 리뷰 작성
        //  - 상품 구매 내역이 없는 경우 리뷰 작성 불가(예외 처리)
        // 매장 테이블 밑에 리뷰 테이블을 만들어서 리뷰를 생성
        Review review = reviewRepository.save(new Review(reviewRequest, storeId, user));
        return new ReviewResponse(review);
    }
}
