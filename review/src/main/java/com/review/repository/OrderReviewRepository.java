package com.review.repository;

import com.review.entity.OrderReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderReviewRepository extends JpaRepository<OrderReview, Long> {
}
