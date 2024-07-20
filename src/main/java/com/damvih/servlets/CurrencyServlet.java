package com.damvih.servlets;

import com.damvih.dao.CurrencyJdbcDao;
import com.damvih.dto.ResponseData;
import com.damvih.entities.Currency;
import com.damvih.exceptions.NotFoundException;
import com.damvih.utils.Checker;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    private final CurrencyJdbcDao currencyJdbcDao = new CurrencyJdbcDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = getPathValue(request, "code");
        Checker.validateCode(code);
        Currency currency = currencyJdbcDao
                .findByCode(code)
                .orElseThrow(() -> new NotFoundException("currency"));
        sendResponse(response, new ResponseData<>(currency, HttpServletResponse.SC_OK));
    }

}
