package com.example.demo.service;

import com.example.demo.dto.PaymentSuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final String TOPIC = "payment.success";

    @Autowired
    private KafkaTemplate<String, PaymentSuccessMessage> kafkaTemplate;

    public void sendPaymentSuccessMessage(PaymentSuccessMessage message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
