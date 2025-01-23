package com.example.demo.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentSuccessMessage {
    private String orderId;
    private List<OrderItem> items;
}
