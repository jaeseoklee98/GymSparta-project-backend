package com.sparta.fltpleprojectbackend.review.entity;

import com.sparta.fltpleprojectbackend.common.TimeStamped;
import com.sparta.fltpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fltpleprojectbackend.usermembership.entity.UserMembership;
import com.sparta.fltpleprojectbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class StoreReview extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_membership_id", nullable = false)
    private UserMembership userMembership;

    // @Column(nullable = false)
    // private Long paymentId; // Payment ID (commented out)

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String comment;

    public StoreReview(User user, UserMembership userMembership, ReviewRequest reviewRequest) {
        this.user = user;
        this.userMembership = userMembership;
        this.rating = reviewRequest.getRating();
        this.comment = reviewRequest.getComment();
    }

    public void update(ReviewRequest reviewRequest) {
        this.rating = reviewRequest.getRating();
        this.comment = reviewRequest.getComment();
    }
}