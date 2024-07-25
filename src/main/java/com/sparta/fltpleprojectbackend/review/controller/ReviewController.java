package com.sparta.fltpleprojectbackend.review.controller;

import com.sparta.fltpleprojectbackend.common.CommonResponse;
import com.sparta.fltpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fltpleprojectbackend.review.dto.ReviewResponse;
import com.sparta.fltpleprojectbackend.review.service.ReviewService;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders/users/{ordersId}/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 생성
     *
     * @param storeId 매장 ID. 매장을 이용한 사용자만 리뷰를 작성할 수 있습니다.
     * @param reviewRequest 리뷰 생성 요청 데이터. 사용자의 리뷰 내용을 포함합니다.
     * @return 생성된 리뷰에 대한 응답 데이터.
     */
    @PostMapping("")
    public ResponseEntity<CommonResponse<ReviewResponse>> createReview(
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storeId,
            @Valid @RequestBody ReviewRequest reviewRequest
    ) {
        ReviewResponse reviewResponse = reviewService.createReview(reviewRequest, storeId
                //        , userDetails.getUser()
        );
        CommonResponse<ReviewResponse> response = new CommonResponse<>(
                HttpStatus.CREATED.value(), "리뷰 등록 완료", reviewResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /*
    @GetMapping
    public ResponseEntity<CommonResponse<List<ReviewResponse>>> getReviews(
            @PathVariable Long storeId
            //, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<ReviewResponse> reviewResponseList = reviewService.getReviews(storeId
                //,userDetails.getUser()
        );

        CommonResponse<List<ReviewResponse>> response = new CommonResponse<>(
                HttpStatus.CREATED.value(), "리뷰 조회 완료", reviewResponseList);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

     */

    /**
     * 리뷰 수정
     *
     * @param reviewId 리뷰 ID. 수정할 리뷰의 고유 식별자입니다.
     * @param reviewRequest 리뷰 수정 요청 데이터. 수정할 리뷰의 내용을 포함합니다.
     * @return 수정된 리뷰에 대한 응답 데이터.
     * @description 리뷰 작성한 사용자만 리뷰를 수정할 수 있습니다.
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<ReviewResponse>> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequest reviewRequest
                                                                       //        , @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewResponse reviewResponse = reviewService.updateReview(reviewId, reviewRequest
                //        , userDetails.getUser()
        );
        CommonResponse<ReviewResponse> response = new CommonResponse<>(
                HttpStatus.OK.value(), "리뷰 수정 완료", reviewResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 리뷰 삭제
     *
     * @param reviewId 리뷰 ID. 삭제할 리뷰의 고유 식별자입니다.
     * @description 리뷰 작성한 사용자만 리뷰를 삭제할 수 있습니다. 관리자 권한으로 부적절한 리뷰 삭제는 별도의 기능으로 구현됩니다.
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<ReviewResponse>> deleteReview(
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId
                //        , userDetails.getUser()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
