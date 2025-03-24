package com.shopee.microservices.order.controller;

import com.shopee.microservices.order.dto.OrderRequest;
import com.shopee.microservices.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping(produces="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,String> placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return Collections.singletonMap("message", "Order placed successfully");
    }
}
