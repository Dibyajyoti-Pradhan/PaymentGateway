package com.checkout.payment.gateway.processor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.checkout.payment.gateway.model.PaymentResponse;

@SpringBootTest
public class PaymentProcessorTest {
    @Autowired
    private PaymentProcessor paymentProcessor;

    @Test
    void testCreatePaymentWhenInputIsValidThenStoreAndValidate() {
        PaymentResponse response = new PaymentResponse(
                "123",
                "Authorized",
                "1234567892",
                4,
                24,
                "USD",
                30);
        paymentProcessor.createPayment(response);
        assertTrue(paymentProcessor.getPaymentResponseFromId(response.getId()) == response);

    }

}