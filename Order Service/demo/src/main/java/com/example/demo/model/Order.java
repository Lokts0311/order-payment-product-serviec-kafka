package com.example.demo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order {

    private String orderId;
    private String userId;
    private String status;
}
