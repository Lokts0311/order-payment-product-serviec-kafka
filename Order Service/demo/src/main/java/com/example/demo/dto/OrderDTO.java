package com.example.demo.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {
    private String orderId;
    private String userId;
    private String status;
    private List<OrderItem> items;
}

