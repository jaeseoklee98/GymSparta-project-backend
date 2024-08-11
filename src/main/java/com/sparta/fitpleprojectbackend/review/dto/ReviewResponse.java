package com.sparta.fitpleprojectbackend.review.dto;

import com.sparta.fitpleprojectbackend.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponse {

    private Long id;
    private int rating;
    private String comment;
    private String reviewType;
    private Long storeId;
    private Long trainerId;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.reviewType = review.getReviewType().name();
        this.storeId = review.getStore() != null ? review.getStore().getId() : null;
        this.trainerId = review.getTrainer() != null ? review.getTrainer().getId() : null;
    }
}