package com.damvih.servlets;

import com.damvih.dto.ExchangeRequest;
import com.damvih.dto.ExchangeResponse;
import com.damvih.dto.ResponseData;
import com.damvih.services.ExchangeService;
import com.damvih.utils.Checker;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends BaseServlet {

    private final ExchangeService exchangeService = new ExchangeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String baseCurrencyCode = request.getParameter("from");
        String targetCurrencyCode = request.getParameter("to");
        String amount = request.getParameter("amount");

        Checker.validateCode(baseCurrencyCode);
        Checker.validateCode(targetCurrencyCode);
        Checker.validateRate(amount);

        BigDecimal amountValue = new BigDecimal(amount);

        ExchangeResponse exchangeResponse = exchangeService.convertCurrency(new ExchangeRequest(
                baseCurrencyCode,
                targetCurrencyCode,
                amountValue
        ));
        sendResponse(response, new ResponseData<>(exchangeResponse, HttpServletResponse.SC_OK));
    }

}

