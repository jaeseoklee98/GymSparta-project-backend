package com.sparta.fltpleprojectbackend.review.repository;

import com.sparta.fltpleprojectbackend.review.entity.review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<review, Long> {
}
