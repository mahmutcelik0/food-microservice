package com.order.service;

import com.order.client.ProductClient;
import com.order.client.RestaurantClient;
import com.order.client.UserClient;
import com.order.constants.OrderConstants;
import com.order.entity.Order;
import com.order.entity.ProductId;
import com.order.exception.OrderNotFoundException;
import com.order.model.CustomResponseMessages;
import com.order.model.ResponseMessage;
import com.order.model.request.OrderRequest;
import com.order.model.response.OrderResponse;
import com.order.model.response.ProductResponse;
import com.order.model.response.RestaurantResponse;
import com.order.model.response.UserResponse;
import com.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantClient restaurantClient;
    private final ProductClient productClient;
    private final UserClient userClient;

    public ResponseEntity<ResponseMessage> createOrder(OrderRequest orderRequest, String userEmail) {
        if(restaurantClient.existById(orderRequest.getRestaurantId())) return ResponseEntity.internalServerError().body(new ResponseMessage(CustomResponseMessages.RESTAURANT_DOES_NOT_EXISTS,HttpStatus.INTERNAL_SERVER_ERROR));
        List<ProductResponse> productResponse = productClient.getAllById(orderRequest.getProductIdList());
        UserResponse userResponse = userClient.getUser(userEmail);
        if (!checkOrderPrice(productResponse, orderRequest.getTotalPrice()))
            return ResponseEntity.internalServerError().body(new ResponseMessage(CustomResponseMessages.ORDER_NOT_CREATED, HttpStatus.INTERNAL_SERVER_ERROR));
        Order order = Order.builder()
                .restaurantId(orderRequest.getRestaurantId())
                .productIdList(orderRequest.getProductIdList().stream().map(ProductId::new).collect(Collectors.toSet()))
                .userId(userResponse.getId())
                .totalPrice(orderRequest.getTotalPrice())
                .build();
        orderRepository.save(order);

        return ResponseEntity.ok(new ResponseMessage(CustomResponseMessages.ORDER_CREATED, HttpStatus.OK));
    }

    private boolean checkOrderPrice(List<ProductResponse> productResponse, Long totalPrice) {
        AtomicLong temp = new AtomicLong();
        productResponse.forEach(e -> temp.addAndGet(e.getProductPrice()));
        return (temp.get() + OrderConstants.deliveryChange) == totalPrice;
    }

    public ResponseEntity<OrderResponse> getOrderByOrderId(Long orderId, String userEmail) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(CustomResponseMessages.ORDER_NOT_FOUND));
        RestaurantResponse restaurantResponse = restaurantClient.getById(order.getRestaurantId());
        List<ProductResponse> productResponseList = productClient.getAllById(order.getProductIdList().stream().map(ProductId::getProductId).toList());
        UserResponse userResponse = userClient.getUser(userEmail);
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(orderId)
                .restaurantResponse(restaurantResponse)
                .productResponseList(productResponseList)
                .totalPrice(order.getTotalPrice())
                .userResponse(userResponse)
                .build();

        return ResponseEntity.ok(orderResponse);
    }

    public List<OrderResponse> getOrdersOfRestaurantById(Long restaurantId, String userEmail) {
        List<Order> orderList = orderRepository.findOrdersByRestaurantId(restaurantId);

        return orderList.stream().map(e -> OrderResponse.builder()
                .orderId(e.getId())
                .restaurantResponse(restaurantClient.getById(e.getRestaurantId()))
                .productResponseList(productClient.getAllById(e.getProductIdList().stream().map(ProductId::getId).toList()))
                .totalPrice(e.getTotalPrice())
                .build()).toList();
    }
}
