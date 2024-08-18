package com.sparta.gymspartaprojectbackend.wishlist.service;

import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
import com.sparta.gymspartaprojectbackend.wishlist.entity.Wistlist;
import com.sparta.gymspartaprojectbackend.wishlist.enums.WishType;
import com.sparta.gymspartaprojectbackend.wishlist.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WishlistService {
  @Autowired
  private WishlistRepository wishlistRepository;

  public void toggleWishlist(UserDetailsImpl userDetails, WishType wishType) {
    Wistlist wishlist = wishlistRepository.findByUserIdAndWishType(userDetails.getUserId(), wishType);
    if (wishlist != null) {
      wishlist.toggleWish();
      wishlistRepository.save(wishlist);
    } else {
      Wistlist newWishlist = new Wistlist(userDetails.getUser(), wishType, true);
      wishlistRepository.save(newWishlist);
    }
  }

  public List<Wistlist> getWishlistByUserId(Long userId) {
    return wishlistRepository.findByUserId(userId);
  }
}