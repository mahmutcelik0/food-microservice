package com.review.dto;

public record CreateReviewRequest(int orderId, String reviewBody, int star) {
}
