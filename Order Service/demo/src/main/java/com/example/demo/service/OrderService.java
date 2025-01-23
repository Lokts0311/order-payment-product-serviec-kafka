package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    RestTemplate restTemplate;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .orderId("123")
                .userId(orderRequest.getUserId())
                .status("PENDING")
                .build();

        PaymentRequest paymentRequest = PaymentRequest.builder().orderId(order.getOrderId()).items(orderRequest.getOrderItems()).build();

        ResponseEntity<PaymentResponse> response = restTemplate.postForEntity("http://localhost:8081" + "/payments", paymentRequest, PaymentResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            PaymentResponse paymentResponse = response.getBody();
            return OrderResponse.builder().paymentUrl(paymentResponse.getPaymentUrl()).build();
        } else {
            throw new RuntimeException("Failed to create payment");
        }
    }
}
