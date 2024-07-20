package com.damvih.filters;

import com.damvih.dto.ExceptionResponse;
import com.damvih.dto.ResponseData;
import com.damvih.exceptions.DatabaseOperationException;
import com.damvih.exceptions.MissedParameterException;
import com.damvih.exceptions.AlreadyExistsException;
import com.damvih.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;

@WebFilter("/*")
public class ExceptionFilter implements Filter {

    private static final HashMap<String, Integer> codes = new HashMap<>();
    static {
        codes.put(DatabaseOperationException.class.getSimpleName(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        codes.put(AlreadyExistsException.class.getSimpleName(), HttpServletResponse.SC_CONFLICT);
        codes.put(NotFoundException.class.getSimpleName(), HttpServletResponse.SC_NOT_FOUND);
        codes.put(InvalidParameterException.class.getSimpleName(), HttpServletResponse.SC_BAD_REQUEST);
        codes.put(MissedParameterException.class.getSimpleName(), HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception exception) {
            ObjectMapper objectMapper = new ObjectMapper();
            ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage());
            int code = selectResponseErrorStatus(exception);
            ResponseData<ExceptionResponse> responseData = new ResponseData<>(exceptionResponse, code);
            ((HttpServletResponse) response).setStatus(code);
            objectMapper.writeValue(response.getWriter(), responseData.getPayload());
        }
    }

    private static int selectResponseErrorStatus(Exception exception) {
        String exceptionName = exception.getClass().getSimpleName();
        if (codes.containsKey(exceptionName)) {
            return codes.get(exceptionName);
        }
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

}
