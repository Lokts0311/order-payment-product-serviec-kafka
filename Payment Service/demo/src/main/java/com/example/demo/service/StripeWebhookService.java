package com.example.demo.service;

import com.example.demo.dto.PaymentSuccessMessage;
import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeWebhookService {

    @Value("${stripe.webhook.secretKey}")
    private String stripeWebhookSecret;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    KafkaProducer kafkaProducer;

    public void handleStripeWebhook (String payload, String sigHeader) throws Exception {

        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);

        } catch (SignatureVerificationException e) {
            throw new Exception("Invalid webhook signature " + e.getMessage() );
        }

        if("checkout.session.completed".equals(event.getType())) {

            ObjectMapper objectMapper = new ObjectMapper();
            String eventSessionId = objectMapper.readTree(event.getData().toJson())
                    .get("object").get("id")
                    .asText(); // toString() will not work, as it output entire JSON node structure, but not the actual string value

            // Update Order status with given payment intent
            Payment payment = paymentRepository.findBySessionId(eventSessionId)
                    .orElseThrow(() -> new RuntimeException("cannot find match session id in DB"));
            payment.setStatus("PAID");
            payment = paymentRepository.save(payment);

            // Produce Kafka message to Product Service
            System.out.println("This is my order id " + payment.getOrderId());
            PaymentSuccessMessage successMessage = PaymentSuccessMessage.builder()
                    .orderId(payment.getOrderId())
                    .items(payment.getItems())
                    .build();

            kafkaProducer.sendPaymentSuccessMessage(successMessage);
        }
    }
}
