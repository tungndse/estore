package com.coldev.estore.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Order(Ordered.LOWEST_PRECEDENCE)
//@Component
public class CustomResponseFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        responseCacheWrapperObject.copyBodyToResponse();
        byte[] responseArray = responseCacheWrapperObject.getContentAsByteArray();
        String responseStr = new String(responseArray, responseCacheWrapperObject.getCharacterEncoding());
        if (responseStr.contains("\"data\":null")) {
            responseStr = responseStr.replace("\"data\":null", "\"data\":[]");
        }


        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println(responseStr);
        objectMapper.writeValue(responseCacheWrapperObject.getOutputStream(), responseStr);

        servletResponse.setContentType("application/json");

        //....use responsestr to make the signature

        // responseCacheWrapperObject.setStatus(201);
        filterChain.doFilter(servletRequest, responseCacheWrapperObject);
    }
}
