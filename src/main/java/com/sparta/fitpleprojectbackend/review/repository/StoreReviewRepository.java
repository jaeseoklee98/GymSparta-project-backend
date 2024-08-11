package com.sparta.fitpleprojectbackend.review.repository;

import com.sparta.fitpleprojectbackend.review.entity.StoreReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {
    List<StoreReview> findByUserId(Long userId);

    Optional<StoreReview> findByUserMembershipId(Long usermembershipId);
}