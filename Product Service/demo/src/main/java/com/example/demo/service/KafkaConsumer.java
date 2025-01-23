package com.example.demo.service;


import com.example.demo.dto.PaymentSuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    private ProductService productService;

    @KafkaListener(topics = "payment.success", groupId = "product-service.payment-success", containerFactory = "kafkaListenerContainerFactory")
    public void consume(PaymentSuccessMessage message) {
        System.out.println("Received Message: " + message);

        productService.decrementStock(message);
    }
}
