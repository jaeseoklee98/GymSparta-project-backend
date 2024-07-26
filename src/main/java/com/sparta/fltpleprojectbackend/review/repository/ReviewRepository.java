package com.sparta.fltpleprojectbackend.review.repository;

import com.sparta.fltpleprojectbackend.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByOrdersId(Long ordersId);
}
