package com.sparta.fltpleprojectbackend.review.dto;

import com.sparta.fltpleprojectbackend.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private int rating;
    private String comment;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
    }
}
