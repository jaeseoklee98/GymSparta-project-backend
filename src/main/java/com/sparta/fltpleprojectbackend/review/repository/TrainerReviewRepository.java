package com.sparta.fltpleprojectbackend.review.repository;

import com.sparta.fltpleprojectbackend.review.entity.TrainerReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TrainerReviewRepository extends JpaRepository<TrainerReview, Long> {
    List<TrainerReview> findByUserPt_User_Id(Long userId);
    List<TrainerReview> findByUserPt_Id(Long userPtId);

    Optional<TrainerReview> findByUserPt_Trainer_Id(Long trainerId);
}