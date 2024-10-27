package com.checkout.payment.gateway.model;

import lombok.Data;

@Data
public class PaymentResponse {
    private String id;
    private String status;
    private String lastFourCardDigits;
    private Integer expiryMonth;
    private Integer expiryYear;
    private String currency;
    private Integer amount;

    public PaymentResponse(String id, String status, String cardNumber, Integer expiryMonth, Integer expiryYear,
            String currency, Integer amount) {
        this.id = id;
        this.status = status;
        this.lastFourCardDigits = cardNumber.length() > 4 ? cardNumber.substring(cardNumber.length() - 4) : cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.currency = currency;
        this.amount = amount;
    }

}
