package com.order.repository;

import com.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
      List<Order> findOrdersByRestaurantId(Long restaurantId);
    List<Order> findByUserId(Long userId);
}
