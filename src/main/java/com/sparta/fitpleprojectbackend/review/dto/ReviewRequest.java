package com.sparta.fitpleprojectbackend.review.dto;

import com.sparta.fitpleprojectbackend.review.enums.ReviewType;
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
    private Long membershipId; // UserMembership ID, if applicable
    private Long userPtId; // UserPt ID, if applicable
    private Long paymentId; // Payment ID, required
}