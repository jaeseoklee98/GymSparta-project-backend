package com.sparta.gymspartaprojectbackend.wishlist.repository;

import com.sparta.gymspartaprojectbackend.wishlist.entity.Wistlist;
import com.sparta.gymspartaprojectbackend.wishlist.enums.WishType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wistlist, Long> {
  Wistlist findByUserIdAndWishType(Long userId, WishType wishType);
  List<Wistlist> findByUserId(Long userId);
}