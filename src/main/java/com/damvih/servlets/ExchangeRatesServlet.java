package com.damvih.servlets;

import com.damvih.dao.CurrencyJdbcDao;
import com.damvih.dao.ExchangeRateJdbcDao;
import com.damvih.dto.ExchangeRateRequest;
import com.damvih.dto.ResponseData;
import com.damvih.entities.Currency;
import com.damvih.entities.ExchangeRate;
import com.damvih.exceptions.NotFoundException;
import com.damvih.utils.Checker;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {

    private final ExchangeRateJdbcDao exchangeRateJdbcDao = new ExchangeRateJdbcDao();
    private final CurrencyJdbcDao currencyJdbcDao = new CurrencyJdbcDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ExchangeRate> exchangeRates = exchangeRateJdbcDao.findAll();
        sendResponse(response, new ResponseData<>(exchangeRates, HttpServletResponse.SC_OK));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rate = request.getParameter("rate");

        Checker.validateRate(rate);
        BigDecimal rateValue = new BigDecimal(rate);

        ExchangeRateRequest exchangeRateRequest = new ExchangeRateRequest(baseCurrencyCode, targetCurrencyCode, rateValue);
        Checker.validateExchangeRate(exchangeRateRequest);

        ExchangeRate exchangeRate = exchangeRateJdbcDao.create(getExchangeRate(exchangeRateRequest));
        sendResponse(response, new ResponseData<>(exchangeRate, HttpServletResponse.SC_CREATED));
    }

    private ExchangeRate getExchangeRate(ExchangeRateRequest exchangeRateRequest) {
        Currency baseCurrency = getCurrency(exchangeRateRequest.getBaseCurrencyCode());
        Currency targetCurrency = getCurrency(exchangeRateRequest.getTargetCurrencyCode());
        return new ExchangeRate(
                baseCurrency,
                targetCurrency,
                exchangeRateRequest.getRate()
        );
    }

    private Currency getCurrency(String code) {
        return currencyJdbcDao
                .findByCode(code)
                .orElseThrow(() -> new NotFoundException("currency"));
    }

}
