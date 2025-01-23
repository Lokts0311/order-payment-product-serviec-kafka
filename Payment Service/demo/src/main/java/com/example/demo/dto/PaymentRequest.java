package com.example.demo.dto;

import com.example.demo.model.OrderItem;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String orderId;
    private List<OrderItem> items;
}
