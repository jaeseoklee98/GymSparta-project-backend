package com.sparta.fltpleprojectbackend.review.repository;

import com.sparta.fltpleprojectbackend.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
