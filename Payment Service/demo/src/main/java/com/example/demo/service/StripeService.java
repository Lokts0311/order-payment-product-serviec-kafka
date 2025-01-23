package com.example.demo.service;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StripeService {

    @Value("${stripe.secretkey}")
    private String secretKey;

    @Autowired
    private PaymentRepository paymentRepository;


    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        Stripe.apiKey = secretKey;

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for(OrderItem item : paymentRequest.getItems()){
            String priceId;
            try {
                priceId = Product.retrieve(item.getProductId()).getDefaultPrice();
            } catch (StripeException e) {
                throw new RuntimeException("Cannot Get Product with product id " + item.getProductId());
            }

            lineItems.add(SessionCreateParams.LineItem.builder()
                    // Provide the exact Price ID (for example, pr_1234) of the product you want to sell
                    .setPrice(priceId)
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    .build());
        }


        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4242/success.html")
                .setCancelUrl("http://localhost:4242/cancel.html")
                .addAllLineItem(lineItems)
                .build();
        Session session;

        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create session: " + e);
        }

        Payment payment = Payment
                .builder()
                .orderId(UUID.randomUUID().toString())
                .items(paymentRequest.getItems())
                .sessionId(session.getId())
                .status("PENDING")
                .build();

        paymentRepository.save(payment);

        return PaymentResponse.builder().paymentUrl(session.getUrl()).build();
    }
}
