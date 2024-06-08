package com.review.controller;

import com.review.dto.CreateReviewRequest;
import com.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public void saveReview(@RequestBody CreateReviewRequest reviewRequest){
        reviewService.saveReview(reviewRequest);
    }


}
