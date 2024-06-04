package com.apigateway.client;

import com.apigateway.config.UserServiceClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users", configuration = UserServiceClientConfig.class)
public interface UserServiceClient {
    @GetMapping("/api/v1/users/auth/validate")
    String validateToken(@RequestHeader("Authorization") String authHeader);
}
