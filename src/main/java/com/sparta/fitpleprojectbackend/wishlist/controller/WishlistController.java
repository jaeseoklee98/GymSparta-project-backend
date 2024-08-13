package com.sparta.fitpleprojectbackend.wishlist.controller;

import com.sparta.fitpleprojectbackend.common.CommonResponse;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.wishlist.dto.WishlistResponse;
import com.sparta.fitpleprojectbackend.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @PostMapping("/store/{storeId}")
    public ResponseEntity<CommonResponse<String>> toggleStoreWishlist(@PathVariable Long storeId,
                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean isWished = wishlistService.toggleStoreWishlist(storeId, userDetails.getUser());
        String message = isWished ? "매장 찜 등록" : "매장 찜 취소";
        return new ResponseEntity<>(new CommonResponse<>(HttpStatus.OK.value(), message, null), HttpStatus.OK);
    }

    @PostMapping("/trainer/{trainerId}")
    public ResponseEntity<CommonResponse<String>> toggleTrainerWishlist(@PathVariable Long trainerId,
                                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean isWished = wishlistService.toggleTrainerWishlist(trainerId, userDetails.getUser());
        String message = isWished ? "트레이너 찜 등록" : "트레이너 찜 취소";
        return new ResponseEntity<>(new CommonResponse<>(HttpStatus.OK.value(), message, null), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<CommonResponse<WishlistResponse>> getUserWishlist(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        WishlistResponse wishlist = wishlistService.getUserWishlist(userDetails.getUser());
        return new ResponseEntity<>(new CommonResponse<>(HttpStatus.OK.value(), "찜 목록 조회 완료", wishlist), HttpStatus.OK);
    }

    @GetMapping("/store/{storeId}/count")
    public ResponseEntity<CommonResponse<Long>> getStoreWishlistCount(@PathVariable Long storeId) {
        long count = wishlistService.getStoreWishlistCount(storeId);
        return new ResponseEntity<>(new CommonResponse<>(HttpStatus.OK.value(), "매장 찜 갯수 조회 완료", count), HttpStatus.OK);
    }

    @GetMapping("/trainer/{trainerId}/count")
    public ResponseEntity<CommonResponse<Long>> getTrainerWishlistCount(@PathVariable Long trainerId) {
        long count = wishlistService.getTrainerWishlistCount(trainerId);
        return new ResponseEntity<>(new CommonResponse<>(HttpStatus.OK.value(), "트레이너 찜 갯수 조회 완료", count), HttpStatus.OK);
    }
}