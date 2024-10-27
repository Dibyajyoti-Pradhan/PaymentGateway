package com.checkout.payment.gateway.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.checkout.payment.gateway.model.PaymentRequest;
import com.checkout.payment.gateway.model.PaymentResponse;
import com.checkout.payment.gateway.service.PaymentGatewayService;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentGatewayControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private PaymentGatewayController paymentGatewayController;

  @Mock
  private PaymentGatewayService paymentGatewayService;

  @Test
  void simpleGetRequest() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/ok").param("message", "test"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("test"));
  }

  @Test
  void simpleGetRequestException() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/nok").param("message", "test"))
        .andExpect(status().is(500))
        .andExpect(jsonPath("$.message").value("Something happened"));
  }

  @Test
  void testProcessPaymentWhenValidRequestThenSuccess() {
    PaymentRequest request = new PaymentRequest("2222405343248877", 04, 2025, "USD", 30, "233");
    PaymentResponse response = new PaymentResponse(
        java.util.UUID.randomUUID().toString(),
        "Authorized",
        request.getCardNumber(),
        request.getExpiryMonth(),
        request.getExpiryYear(),
        request.getCurrency(),
        request.getAmount());
    when(paymentGatewayService.processPayment(any())).thenReturn(response);
    assertTrue(paymentGatewayController.processPayment(request).getStatusCode() == HttpStatus.OK);

  }
}
