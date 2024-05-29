package com.restaurant.controller;

import com.restaurant.dto.request.ProductRequest;
import com.restaurant.exception.RestaurantNotFoundException;
import com.restaurant.model.ResponseMessage;
import com.restaurant.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ResponseMessage> addProduct(@RequestBody ProductRequest product) throws RestaurantNotFoundException {
        return productService.addProduct(product);
    }

}
