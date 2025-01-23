package com.example.demo.controller;

import com.example.demo.service.StripeWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe-webhook")
public class StripeWebhookController {

    @Autowired
    private StripeWebhookService stripeWebhookService;

    @PostMapping
    public ResponseEntity<?> handleStripeWebhook (@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            stripeWebhookService.handleStripeWebhook(payload, sigHeader);
            return ResponseEntity.status(HttpStatus.OK).body("Webhook processed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to process webhook: " + e.getMessage());
        }

    }
}
