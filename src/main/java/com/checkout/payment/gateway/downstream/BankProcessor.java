package com.checkout.payment.gateway.downstream;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.checkout.payment.gateway.model.BankPaymentResponse;
import com.checkout.payment.gateway.model.PaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BankProcessor {
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    public BankProcessor() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper(); // Jackson's ObjectMapper for JSON processing
    }

    public BankPaymentResponse processBankPayment(PaymentRequest paymentRequest) throws Exception {
        String url = "http://localhost:8080/payments";

        Map<String, Object> requestBody = new HashMap<>();

        String month = paymentRequest.getExpiryMonth().toString();
        if (month.length() == 1) {
            month = "0" + month;
        }
        requestBody.put("card_number", paymentRequest.getCardNumber());
        requestBody.put("expiry_date", month + "/" + paymentRequest.getExpiryYear());
        requestBody.put("currency", paymentRequest.getCurrency());
        requestBody.put("amount", paymentRequest.getAmount());
        requestBody.put("cvv", paymentRequest.getCvv());

        String jsonRequest = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonRequest))
                .build();

        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

        Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);

        return new BankPaymentResponse(
                responseMap.get("authorized") == null ? false : (Boolean) responseMap.get("authorized"),
                (String) responseMap.get("authorization_code"));
    }

}
