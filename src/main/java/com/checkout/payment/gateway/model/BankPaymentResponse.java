package com.checkout.payment.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankPaymentResponse {
    Boolean authorized;
    String authorizationCode;
}
