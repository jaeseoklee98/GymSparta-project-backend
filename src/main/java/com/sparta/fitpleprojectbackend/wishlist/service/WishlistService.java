package com.sparta.fitpleprojectbackend.wishlist.service;

import com.sparta.fitpleprojectbackend.store.entity.Store;
import com.sparta.fitpleprojectbackend.store.service.StoreService;
import com.sparta.fitpleprojectbackend.trainer.entity.Trainer;
import com.sparta.fitpleprojectbackend.trainer.service.TrainerService;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.wishlist.dto.WishlistResponse;
import com.sparta.fitpleprojectbackend.wishlist.entity.StoreWishlist;
import com.sparta.fitpleprojectbackend.wishlist.entity.TrainerWishlist;
import com.sparta.fitpleprojectbackend.wishlist.repository.StoreWishlistRepository;
import com.sparta.fitpleprojectbackend.wishlist.repository.TrainerWishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final StoreWishlistRepository storeWishlistRepository;
    private final TrainerWishlistRepository trainerWishlistRepository;
    private final StoreService storeService;
    private final TrainerService trainerService;

    @Transactional
    public boolean toggleStoreWishlist(Long storeId, User user) {
        Store store = storeService.findStoreById(storeId);
        StoreWishlist wishlist = storeWishlistRepository.findByUserAndStoreId(user, storeId)
                .orElseGet(() -> new StoreWishlist(user, store));
        if(wishlist.isWish()){
            storeWishlistRepository.delete(wishlist);
            return false;
        }
        wishlist.update();
        storeWishlistRepository.save(wishlist);
        return wishlist.isWish();
    }

    @Transactional
    public boolean toggleTrainerWishlist(Long trainerId, User user) {
        Trainer trainer = trainerService.findTrainerById(trainerId);
        TrainerWishlist wishlist = trainerWishlistRepository.findByUserAndTrainerId(user, trainerId)
                .orElseGet(() -> new TrainerWishlist(user, trainer));
        wishlist.update();
        trainerWishlistRepository.save(wishlist);
        return wishlist.isWish();
    }

    public WishlistResponse getUserWishlist(User user) {
        List<StoreWishlist> storeWishlists = storeWishlistRepository.findByUserId(user.getId());
        List<TrainerWishlist> trainerWishlists = trainerWishlistRepository.findByUserId(user.getId());
        return new WishlistResponse(storeWishlists, trainerWishlists);
    }

    public long getStoreWishlistCount(Long storeId) {
        return storeWishlistRepository.countByStoreId(storeId);
    }

    public long getTrainerWishlistCount(Long trainerId) {
        return trainerWishlistRepository.countByTrainerId(trainerId);
    }
}