package com.sparta.fitpleprojectbackend.wishlist.repository;

import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.wishlist.entity.StoreWishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreWishlistRepository extends JpaRepository<StoreWishlist, Long> {
    Optional<StoreWishlist> findByUserAndStoreId(User user, Long storeId);
    List<StoreWishlist> findByUserId(Long userId);
    long countByStoreId(Long storeId);
}