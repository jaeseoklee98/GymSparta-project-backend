package com.sparta.gymspartaprojectbackend.wishlist.dto;

import com.sparta.gymspartaprojectbackend.wishlist.enums.WishType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistResponse {
  private Long id;
  private Long userId;
  private WishType wishType;
  private boolean isWish;
}