package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.downstream.BankProcessor;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.BankPaymentResponse;
import com.checkout.payment.gateway.model.PaymentRequest;
import com.checkout.payment.gateway.model.PaymentResponse;
import com.checkout.payment.gateway.model.RequestEvent;
import com.checkout.payment.gateway.model.ResponseEvent;
import com.checkout.payment.gateway.processor.PaymentProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {
  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);
  @Autowired
  private BankProcessor bankProcessor;
  @Autowired
  private PaymentProcessor paymentProcessor;

  public ResponseEvent processRequest(RequestEvent event, boolean ok) {
    LOG.info("Processing event {}", event);

    if (!ok) {
      throw new EventProcessingException("Wupssss...");
    }

    return new ResponseEvent(event.getMessage());
  }

  public PaymentResponse processPayment(PaymentRequest request) {
    if (!request.isCardExpiryValid()) {

      return getFailedResponse(request);
    }
    try {
      LOG.info("Calling BankProcessor");
      BankPaymentResponse bankResponse = bankProcessor.processBankPayment(request);
      LOG.info("Bank Response {}", bankResponse);

      if (bankResponse.getAuthorized() == false) {
        return getFailedResponse(request);
      }

      PaymentResponse response = new PaymentResponse(
          java.util.UUID.randomUUID().toString(),
          "Authorized",
          request.getCardNumber(),
          request.getExpiryMonth(),
          request.getExpiryYear(),
          request.getCurrency(),
          request.getAmount());
      paymentProcessor.createPayment(response);
      return response;
    } catch (Exception e) {
      LOG.error("Bank Response Errored {}", e);
      return getFailedResponse(request);
    }
  }

  public PaymentResponse retrievePaymentDetails(String id) {
    return paymentProcessor.getPaymentResponseFromId(id);
  }

  private PaymentResponse getFailedResponse(PaymentRequest request) {
    return new PaymentResponse(
        java.util.UUID.randomUUID().toString(),
        "Declined",
        request.getCardNumber(),
        request.getExpiryMonth(),
        request.getExpiryYear(),
        request.getCurrency(),
        request.getAmount());

  }
}
