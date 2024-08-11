package com.sparta.fitpleprojectbackend.review.repository;

import com.sparta.fitpleprojectbackend.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}