package com.example.demo.service;

import com.example.demo.dto.OrderItem;
import com.example.demo.dto.PaymentSuccessMessage;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void createProduct(Product product) {
        productRepository.save(product);

    }

    @Retry(name = "optimistic-lock-retry")
    @Transactional
    public void decrementStock(PaymentSuccessMessage message) {
        for(OrderItem item: message.getItems()) {
            Product product = productRepository.findByProductId(item.getProductId());
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
        System.out.println("Success Update stock");
    }

}
