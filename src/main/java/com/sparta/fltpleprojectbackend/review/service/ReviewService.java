package com.sparta.fltpleprojectbackend.review.service;

import com.sparta.fltpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fltpleprojectbackend.review.dto.ReviewResponse;
import com.sparta.fltpleprojectbackend.review.entity.Review;
import com.sparta.fltpleprojectbackend.review.repository.ReviewRepository;
import com.sparta.fltpleprojectbackend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewResponse createReview(ReviewRequest reviewRequest,  User user) {
        // 주문에서 상품 구매 내역에 따라서 리뷰 작성
        //  - 상품 구매 내역이 없는 경우 리뷰 작성 불가(예외 처리)
        // 매장 테이블 밑에 리뷰 테이블을 만들어서 리뷰를 생성
        Review review = reviewRepository.save(new Review(reviewRequest, user));
        return new ReviewResponse(review);
    }

    @Transactional
    public ReviewResponse updateProduct(Long reviewId, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new IllegalArgumentException( //추후에 커스텀 or 통일된 예외 처리로 변경
                        "리뷰가 존재하지 않습니다."
                )
        );

        if(review.getCreatedAt().plusDays(7).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "리뷰는 작성 후 7일 이후에 수정이 불가능합니다."
            );
        }

        review.update(reviewRequest);

        return new ReviewResponse(review);
    }
}
