package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.model.PaymentRequest;
import com.checkout.payment.gateway.model.PaymentResponse;
import com.checkout.payment.gateway.model.RequestEvent;
import com.checkout.payment.gateway.model.ResponseEvent;
import com.checkout.payment.gateway.service.PaymentGatewayService;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentGatewayController {

  private final PaymentGatewayService paymentGatewayService;

  public PaymentGatewayController(PaymentGatewayService paymentGatewayService) {
    this.paymentGatewayService = paymentGatewayService;
  }

  @GetMapping("/ok")
  public ResponseEntity<ResponseEvent> okRequest(RequestEvent event) {
    return new ResponseEntity<>(paymentGatewayService.processRequest(event, true), HttpStatus.OK);
  }

  @GetMapping("/nok")
  public ResponseEntity<ResponseEvent> notRequest(RequestEvent event) {
    return new ResponseEntity<>(paymentGatewayService.processRequest(event, false), HttpStatus.OK);
  }

  @PostMapping("/processPayment")
  public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
    PaymentResponse response = paymentGatewayService.processPayment(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/retrievePaymentDetails")
  public ResponseEntity<PaymentResponse> retrievePaymentDetails(@Valid @RequestParam String id) {
    PaymentResponse response = paymentGatewayService.retrievePaymentDetails(id);
    if (response == null) {
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
