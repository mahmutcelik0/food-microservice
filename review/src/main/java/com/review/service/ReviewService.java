package com.review.service;

import com.review.client.OrderClient;
import com.review.dto.CreateReviewRequest;
import com.review.entity.OrderReview;
import com.review.entity.ProductReview;
import com.review.repository.OrderReviewRepository;
import com.review.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final OrderReviewRepository repository;
    private final ProductReviewRepository productReviewRepository;
    private final OrderClient orderClient;
    public void saveReview(CreateReviewRequest reviewRequest){
        if(!controlOrderIsPaid(reviewRequest.orderId()))
            throw new RuntimeException("order is not paid");


        List<Long> productIds = orderClient.getProductIds(reviewRequest.orderId());
        for(int i = 0; i < productIds.size(); i++){
            Optional<ProductReview> productReview= productReviewRepository.findById(productIds.get(i));
            if(productReview.isEmpty())
                productReviewRepository.save(new ProductReview(productIds.get(i), reviewRequest.star(),1));
            else{
               int reviewTimes = productReview.get().getReviewTimesOfProduct();
               double averageStar = productReview.get().getProductAverageStar();
               double totalStar = reviewTimes * averageStar + reviewRequest.star();
               reviewTimes += 1;
               double lastAverageStar = totalStar/ reviewTimes;
                productReview.get().setReviewTimesOfProduct(reviewTimes);
                productReview.get().setProductAverageStar(lastAverageStar);
                productReviewRepository.save(productReview.get());
            }
        }
        repository.save(new OrderReview(reviewRequest.orderId(),reviewRequest.reviewBody(),reviewRequest.star()));
    }

    private boolean controlOrderIsPaid(int orderId) {
       return orderClient.isOrderPaid(orderId);
    }
}