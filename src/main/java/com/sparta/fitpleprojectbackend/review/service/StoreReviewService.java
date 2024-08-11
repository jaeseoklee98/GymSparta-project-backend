package com.sparta.fitpleprojectbackend.review.service;

import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fitpleprojectbackend.review.dto.ReviewResponse;
import com.sparta.fitpleprojectbackend.review.entity.StoreReview;
import com.sparta.fitpleprojectbackend.review.exception.ReviewException;
import com.sparta.fitpleprojectbackend.review.repository.StoreReviewRepository;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.store.repository.StoreRepository;
import com.sparta.fitpleprojectbackend.user.entity.User;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import com.sparta.fitpleprojectbackend.usermembership.entity.UserMembership;
import com.sparta.fitpleprojectbackend.usermembership.repository.UserMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreReviewService {
    private final StoreReviewRepository storeReviewRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserMembershipRepository userMembershipRepository;

    @Transactional
    public ReviewResponse createStoreReview(ReviewRequest reviewRequest, Long usermembershipId, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_USER));
        UserMembership userMembership = userMembershipRepository.findById(usermembershipId)
                .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_USER_MEMBERSHIP));

        if (!userMembership.getUser().getId().equals(user.getId())) {
            throw new ReviewException(ErrorType.USER_NOT_AUTHORIZED);
        }

        StoreReview storeReview = new StoreReview(user, userMembership, reviewRequest);
        storeReviewRepository.save(storeReview);

        return new ReviewResponse(storeReview);
    }

    @Transactional
    public ReviewResponse updateStoreReview(Long usermembershipId, Long reviewId, ReviewRequest reviewRequest, UserDetailsImpl userDetails) {
        StoreReview storeReview = storeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorType.REVIEW_NOT_FOUND));

        if (!storeReview.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new ReviewException(ErrorType.USER_NOT_AUTHORIZED);
        }

        if (storeReview.getCreatedAt().isBefore(LocalDateTime.now().minusDays(7))) {
            throw new ReviewException(ErrorType.REVIEW_MODIFICATION_PERIOD_EXPIRED);
        }

        storeReview.update(reviewRequest);
        return new ReviewResponse(storeReview);
    }

    /*
    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllStoreReviews(Long storeId) {
        return storeReviewRepository.findByStoreId(storeId).stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }
     */

    @Transactional(readOnly = true)
    public List<ReviewResponse> getUserStoreReviews(UserDetailsImpl userDetails) {
        return storeReviewRepository.findByUserId(userDetails.getUser().getId()).stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviewsByMembershipId(Long usermembershipId) {
        return storeReviewRepository.findByUserMembershipId(usermembershipId).stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }
}