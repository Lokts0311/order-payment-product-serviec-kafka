package com.example.demo.component;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RetryEventListener {

    private static final Logger logger = LoggerFactory.getLogger(RetryEventListener.class);

    private final RetryRegistry retryRegistry;

    public RetryEventListener(RetryRegistry retryRegistry) {
        this.retryRegistry = retryRegistry;
    }

    @PostConstruct
    public void init() {
        Retry retry = retryRegistry.retry("optimistic-lock-retry");
        retry.getEventPublisher()
                .onRetry(event -> logger.info("Retry attempt {} for method '{}'", event.getNumberOfRetryAttempts(), event.getName()))
                .onError(event -> logger.error("Retry failed for method '{}'", event.getName()))
                .onSuccess(event -> logger.info("Retry succeeded for method '{}'", event.getName()));
    }
}