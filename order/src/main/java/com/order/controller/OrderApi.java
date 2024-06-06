package com.order.controller;

import com.order.entity.Order;
import com.order.exception.OrderNotFoundException;
import com.order.model.request.OrderRequest;
import com.order.model.response.OrderResponse;
import com.order.model.response.UserResponse;
import com.order.service.OrderService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderApi {
    private final OrderService orderService;

    @GetMapping
    public String hello(@RequestHeader("userEmail") String email){
        return "hello from" + email;
    }


    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest){
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/order")
    public ResponseEntity<OrderResponse> getOrderByOrderId(@RequestParam Long orderId) throws OrderNotFoundException {
        return orderService.getOrderByOrderId(orderId);
    }

    @GetMapping("/restaurant")
    public List<OrderResponse> getOrdersOfRestaurantById(@RequestParam Long restaurantId){
        return orderService.getOrdersOfRestaurantById(restaurantId);
    }




}