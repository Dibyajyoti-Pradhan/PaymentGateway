package com.checkout.payment.gateway.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.checkout.payment.gateway.model.PaymentResponse;

@Component
public class PaymentProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentProcessor.class);

    Map<String, PaymentResponse> cache;

    public PaymentProcessor() {
        this.cache = new HashMap<>();
    }

    public PaymentResponse getPaymentResponseFromId(String id) {

        return cache.getOrDefault(id, null);

    }

    public void createPayment(PaymentResponse response) {

        LOG.info("Creating Payment Data in Cache {}", response);
        cache.put(response.getId(), response);

    }

}
