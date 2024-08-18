package com.sparta.gymspartaprojectbackend.wishlist.controller;

import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
import com.sparta.gymspartaprojectbackend.wishlist.entity.Wistlist;
import com.sparta.gymspartaprojectbackend.wishlist.enums.WishType;
import com.sparta.gymspartaprojectbackend.wishlist.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

  @Autowired
  private WishlistService wishlistService;

  @PostMapping("/toggle")
  public void toggleWishlist(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam WishType wishType) {
    wishlistService.toggleWishlist(userDetails, wishType);
  }

  @GetMapping
  public List<Wistlist> getWishlistByUserId(@RequestParam Long userId) {
    return wishlistService.getWishlistByUserId(userId);
  }
}