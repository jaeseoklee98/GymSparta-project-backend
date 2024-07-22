package com.sparta.fltpleprojectbackend.review.entity;

import com.sparta.fltpleprojectbackend.common.Timestamped;
import com.sparta.fltpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fltpleprojectbackend.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "review") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Review extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;



    @NotNull
    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



    @NotNull
    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


    @NotNull
    @Column
    private int rating;

    @NotNull
    @Column
    private String comment;

    public Review(ReviewRequest reviewRequest,User user) {
        this.comment = reviewRequest.getComment();
        this.rating = reviewRequest.getRating();
        this.user = user;
        this.orders = orders;
        this.store = store;
    }

    public void update(ReviewRequest reviewRequest) {
        this.comment = reviewRequest.getComment();
        this.rating = reviewRequest.getRating();
    }
}