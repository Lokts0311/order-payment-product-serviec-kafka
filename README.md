# order-payment-product-serviec-kafka
A springboot example of creating order nad process to payment with Stripe API, finally update inventory stock.

# Step
1. Create a order in Order Service
2. Make a request to Payment Service.
3. Payment Service generate Stripe checkout session.
4. Process Payment in client side.
5. Recieve checkout success event through Stripe webhook.
6. Producer in Payment Service produce message to Kafka topic "payment.success".
7. Consumer in Product Service consume message in topic "payment.success".
8. Update inventory stock with retry mechanism by using resillience4j-retry.
