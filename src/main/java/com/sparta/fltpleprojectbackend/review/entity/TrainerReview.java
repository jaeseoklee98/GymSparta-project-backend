package com.sparta.fltpleprojectbackend.review.entity;

import com.sparta.fltpleprojectbackend.common.TimeStamped;
import com.sparta.fltpleprojectbackend.userpt.entity.UserPt;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TrainerReview extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pt_id", nullable = false)
    private UserPt userPt;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String comment;

    public TrainerReview(UserPt userPt, int rating, String comment) {
        this.userPt = userPt;
        this.rating = rating;
        this.comment = comment;
    }

    public void update(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}