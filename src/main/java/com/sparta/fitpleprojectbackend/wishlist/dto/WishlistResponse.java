package com.sparta.fitpleprojectbackend.wishlist.dto;

import com.sparta.fitpleprojectbackend.wishlist.entity.StoreWishlist;
import com.sparta.fitpleprojectbackend.wishlist.entity.TrainerWishlist;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WishlistResponse {
    private List<StoreWishlist> storeWishlists;
    private List<TrainerWishlist> trainerWishlists;
}