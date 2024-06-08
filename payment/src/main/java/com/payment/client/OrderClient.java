package com.payment.client;

import com.payment.model.response.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service",url = "${application.config.orders-url}")
public interface OrderClient {
    @GetMapping("/order")
    OrderResponse getOrderByOrderId(@RequestParam Long orderId, @RequestHeader("userEmail") String userEmail);

    @GetMapping("/order/paid")
    void setOrderAsPaid(@RequestParam Long orderId);
}


