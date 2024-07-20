package com.damvih.servlets;

import com.damvih.dto.ResponseData;
import com.damvih.utils.Checker;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

abstract public class BaseServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected <T> void sendResponse(HttpServletResponse response, ResponseData<T> responseData) {
        try {
            response.setStatus(responseData.getStatus());
            objectMapper.writeValue(response.getWriter(), responseData.getPayload());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected String getPathValue(HttpServletRequest request, String parameterName) {
        String pathValue = request.getPathInfo();
        Checker.validateParameterValue(parameterName, pathValue);
        return pathValue.replace("/", "");
    }

}
