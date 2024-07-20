package com.damvih.services;

import com.damvih.dao.ExchangeRateJdbcDao;
import com.damvih.dto.ExchangeRequest;
import com.damvih.dto.ExchangeResponse;
import com.damvih.entities.ExchangeRate;
import com.damvih.exceptions.NotFoundException;
import com.damvih.mappers.ExchangeRateMapper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {

    private final ExchangeRateJdbcDao exchangeRateJdbcDao = new ExchangeRateJdbcDao();
    private final ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.INSTANCE;
    private static final String USD_CODE = "USD";
    private static final int ROUNDING_ACCURACY = 2;

    public ExchangeResponse convertCurrency(ExchangeRequest exchangeRequest) {
        ExchangeRate exchangeRate = getExchangeRate(
                exchangeRequest.getBaseCurrencyCode(),
                exchangeRequest.getTargetCurrencyCode());
        BigDecimal convertedAmount = exchangeRequest.getAmount().multiply(exchangeRate.getRate());
        ExchangeResponse exchangeResponse = exchangeRateMapper.toDto(exchangeRate);
        exchangeResponse.setAmount(exchangeRequest.getAmount());
        exchangeResponse.setConvertedAmount(convertedAmount.setScale(ROUNDING_ACCURACY, RoundingMode.HALF_UP));
        return exchangeResponse;
    }

    private ExchangeRate getExchangeRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> exchangeRate = exchangeRateJdbcDao.findByCodes(baseCode, targetCode);
        if (exchangeRate.isEmpty()) {
            exchangeRate = getFromReverseExchangeRate(baseCode, targetCode);
        }
        if (exchangeRate.isEmpty()) {
            exchangeRate = getFromCrossExchangeRate(baseCode, targetCode);
        }
        return exchangeRate.orElseThrow(() -> new NotFoundException("exchange rate"));
    }

    private Optional<ExchangeRate> getFromReverseExchangeRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> reversedExchangeRateOptional = exchangeRateJdbcDao.findByCodes(targetCode, baseCode);
        if (reversedExchangeRateOptional.isPresent()) {
            ExchangeRate reversedExchangeRate = reversedExchangeRateOptional.get();
            return Optional.of(new ExchangeRate(
                    reversedExchangeRate.getTargetCurrency(),
                    reversedExchangeRate.getBaseCurrency(),
                    BigDecimal.ONE.divide(reversedExchangeRate.getRate(), MathContext.DECIMAL64)
            ));
        }
        return Optional.empty();
    }

    private Optional<ExchangeRate> getFromCrossExchangeRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> usdBaseExchangeRateOptional = exchangeRateJdbcDao.findByCodes(USD_CODE, baseCode);
        Optional<ExchangeRate> usdTargetExchangeRateOptional = exchangeRateJdbcDao.findByCodes(USD_CODE, targetCode);
        if (usdBaseExchangeRateOptional.isPresent() && usdTargetExchangeRateOptional.isPresent()) {
            ExchangeRate usdBaseExchangeRate = usdBaseExchangeRateOptional.get();
            ExchangeRate usdTargetExchangeRate = usdTargetExchangeRateOptional.get();
            return Optional.of(new ExchangeRate(
                    usdBaseExchangeRate.getTargetCurrency(),
                    usdTargetExchangeRate.getTargetCurrency(),
                    usdTargetExchangeRate.getRate().divide(usdBaseExchangeRate.getRate(), MathContext.DECIMAL64)
            ));
        }
        return Optional.empty();
    }

}
