package com.sparta.fitpleprojectbackend.wishlist.repository;

import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.wishlist.entity.TrainerWishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerWishlistRepository extends JpaRepository<TrainerWishlist, Long> {
    Optional<TrainerWishlist> findByUserAndTrainerId(User user, Long trainerId);
    long countByTrainerId(Long trainerId);

    List<TrainerWishlist> findByUserId(Long userId);
}