package com.damvih.servlets;

import com.damvih.dao.ExchangeRateJdbcDao;
import com.damvih.dto.ResponseData;
import com.damvih.entities.ExchangeRate;
import com.damvih.exceptions.NotFoundException;
import com.damvih.utils.Checker;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {

    private final ExchangeRateJdbcDao exchangeRateJdbcDao = new ExchangeRateJdbcDao();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codePair = getPathValue(request, "codes");

        List<String> codes = getCodes(codePair);
        String baseCode = codes.getFirst();
        String targetCode = codes.getLast();

        ExchangeRate exchangeRate = exchangeRateJdbcDao
                .findByCodes(baseCode, targetCode)
                .orElseThrow(() -> new NotFoundException("exchange rate"));
        sendResponse(response, new ResponseData<>(exchangeRate, HttpServletResponse.SC_OK));
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codePair = getPathValue(request, "codes");
        String requestBody = request.getReader().readLine();
        String rate = requestBody.replace("rate=", "");

        List<String> codes = getCodes(codePair);
        String baseCode = codes.getFirst();
        String targetCode = codes.getLast();

        Checker.validateRate(rate);

        BigDecimal rateValue = new BigDecimal(rate);

        ExchangeRate exchangeRate = exchangeRateJdbcDao
                .findByCodes(baseCode, targetCode)
                .orElseThrow(() -> new NotFoundException("exchange rate"));
        exchangeRate.setRate(rateValue);

        exchangeRate = exchangeRateJdbcDao.update(exchangeRate);
        sendResponse(response, new ResponseData<>(exchangeRate, HttpServletResponse.SC_OK));
    }

    private List<String> getCodes(String codePair) {
        Checker.validateCodePair(codePair);

        String baseCode = codePair.substring(0, 3);
        String targetCode = codePair.substring(3, 6);

        Checker.validateCode(baseCode);
        Checker.validateCode(targetCode);

        return Arrays.asList(baseCode, targetCode);
    }

}
