package com.sparta.gymspartaprojectbackend.review.repository;

import com.sparta.gymspartaprojectbackend.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  List<Review> findByUserId(Long userId);

  List<Review> findReportedReviewsByStoreId(Long storeId);

  @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store.id = :storeId")
  Double findAverageRatingByStoreId(@Param("storeId") Long storeId);

  @Query("SELECT AVG(r.rating) FROM Review r WHERE r.trainer.id = :trainerId")
  Double findAverageRatingByTrainerId(@Param("trainerId") Long trainerId);

  @Query("SELECT r FROM Review r WHERE r.store.id = :storeId")
  List<Review> findReviewsByStoreId(@Param("storeId") Long storeId);

  @Query("SELECT r FROM Review r WHERE r.trainer.id = :trainerId")
  List<Review> findReviewsByTrainerId(@Param("trainerId") Long trainerId);
}