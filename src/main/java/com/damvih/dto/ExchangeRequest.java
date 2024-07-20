package com.damvih.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequest {

    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal amount;

}
