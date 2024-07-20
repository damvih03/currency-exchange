package com.damvih.servlets;

import com.damvih.dao.CurrencyJdbcDao;
import com.damvih.dto.CurrencyRequest;
import com.damvih.dto.ResponseData;
import com.damvih.entities.Currency;
import com.damvih.mappers.CurrencyMapper;
import com.damvih.utils.Checker;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {

    private final CurrencyJdbcDao currencyJdbcDao = new CurrencyJdbcDao();
    private final CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Currency> currencies = currencyJdbcDao.findAll();
        sendResponse(response, new ResponseData<>(currencies, HttpServletResponse.SC_OK));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");

        CurrencyRequest currencyRequest = new CurrencyRequest(code, name, sign);

        Checker.validateCurrency(currencyRequest);

        Currency currency = currencyJdbcDao.create(currencyMapper.toEntity(currencyRequest));
        sendResponse(response, new ResponseData<>(currency, HttpServletResponse.SC_OK));
    }

}
