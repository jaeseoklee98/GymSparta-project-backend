package com.sparta.fltpleprojectbackend.review.service;

import com.sparta.fltpleprojectbackend.enums.ErrorType;
import com.sparta.fltpleprojectbackend.review.dto.ReviewRequest;
import com.sparta.fltpleprojectbackend.review.dto.ReviewResponse;
import com.sparta.fltpleprojectbackend.review.entity.TrainerReview;
import com.sparta.fltpleprojectbackend.review.exception.ReviewException;
import com.sparta.fltpleprojectbackend.review.repository.TrainerReviewRepository;
import com.sparta.fltpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fltpleprojectbackend.userpt.entity.UserPt;
import com.sparta.fltpleprojectbackend.userpt.repository.UserPtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerReviewService {
    private final TrainerReviewRepository trainerReviewRepository;
    private final UserPtRepository userPtRepository;

    @Transactional
    public ReviewResponse createTrainerReview(ReviewRequest reviewRequest, Long userPtId, UserDetailsImpl userDetails) {
        UserPt userPt = userPtRepository.findById(userPtId)
                .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_USER_PT));

        if (!userPt.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new ReviewException(ErrorType.USER_NOT_AUTHORIZED);
        }

        TrainerReview trainerReview = new TrainerReview(userPt, reviewRequest.getRating(), reviewRequest.getComment());
        trainerReviewRepository.save(trainerReview);

        return new ReviewResponse(trainerReview);
    }

    @Transactional
    public ReviewResponse updateTrainerReview(Long userPtId, Long reviewId, ReviewRequest reviewRequest, UserDetailsImpl userDetails) {
        TrainerReview trainerReview = trainerReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorType.REVIEW_NOT_FOUND));

        if (!trainerReview.getUserPt().getUser().getId().equals(userDetails.getUser().getId())) {
            throw new ReviewException(ErrorType.USER_NOT_AUTHORIZED);
        }

        if (trainerReview.getCreatedAt().isBefore(LocalDateTime.now().minusDays(7))) {
            throw new ReviewException(ErrorType.REVIEW_MODIFICATION_PERIOD_EXPIRED);
        }

        trainerReview.update(reviewRequest.getRating(), reviewRequest.getComment());
        return new ReviewResponse(trainerReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getUserTrainerReviews(UserDetailsImpl userDetails) {
        return trainerReviewRepository.findByUserPt_User_Id(userDetails.getUser().getId()).stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getTrainerReviewsByUserPtId(Long userPtId) {
        UserPt userPt = userPtRepository.findById(userPtId)
                .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND_USER_PT));

        Long trainerId = userPt.getTrainer().getId();
        return trainerReviewRepository.findByUserPt_Trainer_Id(trainerId).stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }
}