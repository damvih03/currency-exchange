package com.damvih.utils;

import com.damvih.dto.CurrencyRequest;
import com.damvih.dto.ExchangeRateRequest;
import com.damvih.exceptions.MissedParameterException;

import java.security.InvalidParameterException;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

// TODO: Check regular expression
public class Checker {

    private static final String INVALID_CODE = "Currency code must be in ISO 4217 format";
    private static final String PATTERN_BIG_DECIMAL_RATE = "^[0-9]+\\.*[0-9]*$";
    private static final Set<String> currencyCodes = Currency
            .getAvailableCurrencies()
            .stream()
            .map(Currency::toString)
            .collect(Collectors.toSet());
    private static final int CODES_LENGTH = 6;
    private static final int SIGN_LENGTH = 1;

    private Checker() {

    }

    public static void validateCode(String code) {
        if (!currencyCodes.contains(code)) {
            throw new InvalidParameterException(INVALID_CODE);
        }
    }

    public static void validateCodePair(String codePair) {
        validateParameterValue("code pair", codePair);
        if (codePair.length() != CODES_LENGTH) {
            throw new InvalidParameterException(INVALID_CODE);
        }
    }

    public static void validateCurrency(CurrencyRequest currencyRequest) {
        String code = currencyRequest.getCode();
        String name = currencyRequest.getName();
        String sign = currencyRequest.getSign();

        validateParameterValue("code", code);
        validateParameterValue("name", name);
        validateParameterValue("sign", sign);
        validateCode(code);

        if (sign.length() != SIGN_LENGTH) {
            throw new InvalidParameterException("Invalid parameter: sign. Require length is 1");
        }
    }

    public static void validateParameterValue(String parameterName, String parameterValue) {
        if (parameterValue == null || parameterValue.isBlank()) {
            throw new MissedParameterException(parameterName);
        }
    }

    public static void validateExchangeRate(ExchangeRateRequest exchangeRateRequest) {
        String baseCurrencyCode = exchangeRateRequest.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequest.getTargetCurrencyCode();

        validateParameterValue("baseCurrencyCode", baseCurrencyCode);
        validateParameterValue("targetCurrencyCode", targetCurrencyCode);
        validateCode(baseCurrencyCode);
        validateCode(targetCurrencyCode);
    }

    public static void validateRate(String rate) {
        validateParameterValue("rate", rate);
        if (!rate.matches(PATTERN_BIG_DECIMAL_RATE)) {
            throw new InvalidParameterException("Invalid parameter: rate");
        }
    }

}
