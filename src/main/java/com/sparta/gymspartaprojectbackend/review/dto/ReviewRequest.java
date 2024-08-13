package com.sparta.gymspartaprojectbackend.review.dto;

import com.sparta.gymspartaprojectbackend.review.enums.ReviewType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

    private int rating;
    private String comment;
    private ReviewType reviewType; // STORE or TRAINER
    private Long storeId; // Store ID, if applicable
    private Long trainerId; // Trainer ID, if applicable
    private Long productId; // Product ID, instead of UserMembership or UserPt
    private Long paymentId; // Payment ID, required
}