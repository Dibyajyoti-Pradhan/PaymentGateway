package com.checkout.payment.gateway.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.YearMonth;

@Data
@AllArgsConstructor
public class PaymentRequest {
    @NotNull
    @Size(min = 14, max = 19)
    @Pattern(regexp = "\\d+")
    private String cardNumber;

    @NotNull
    @Min(1)
    @Max(12)
    private Integer expiryMonth;

    @NotNull
    private Integer expiryYear;

    @NotNull
    @Pattern(regexp = "[A-Z]{3}")
    private String currency;

    @NotNull
    private Integer amount;

    @NotNull
    @Size(min = 3, max = 4)
    @Pattern(regexp = "\\d+")
    private String cvv;

    public boolean isCardExpiryValid() {
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth cardExpiry = YearMonth.of(expiryYear, expiryMonth);
        return cardExpiry.isAfter(currentYearMonth);
    }
}
