package com.aseemsavio.blog.services;

import com.aseemsavio.blog.pojos.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static com.aseemsavio.blog.utils.BlogConstants.*;

@Component
public class RequestFilter implements Filter {

    @Autowired
    AuthService authService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String uri = EMPTY_STRING;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        if (httpRequest instanceof HttpServletRequest)
            uri = httpRequest.getRequestURI();

        if (uri.contains("/secure/")) {
            Enumeration<String> headers = httpRequest.getHeaderNames();
            String accessToken = EMPTY_STRING;
            String header = EMPTY_STRING;
            boolean canProceed = false;
            if (headers != null) {
                while (headers.hasMoreElements()) {
                    header = headers.nextElement();
                    if (header.equals(ACCESS_TOKEN_HEADER)) {
                        canProceed = true;
                        break;
                    }
                }
                if (!canProceed) {
                    ErrorResponse errorResponse = new ErrorResponse(EC_ACCESS_TOKEN_HEADER_NOT_FOUND, EM_ACCESS_TOKEN_HEADER_NOT_FOUND);
                    sendServletResponse(servletResponse, errorResponse);
                    return;
                } else {
                    accessToken = httpRequest.getHeader(header);
                    if (!userFound(accessToken)) {
                        ErrorResponse errorResponse = new ErrorResponse(EC_USER_NOT_FOUND, EM_USER_NOT_FOUND);
                        sendServletResponse(servletResponse, errorResponse);
                        return;
                    }
                }
            } else {
                ErrorResponse errorResponse = new ErrorResponse(EC_ACCESS_TOKEN_HEADER_NOT_FOUND, EM_ACCESS_TOKEN_HEADER_NOT_FOUND);
                sendServletResponse(servletResponse, errorResponse);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean userFound(String accessToken) {
        return authService.userFound(accessToken);
    }

    private void sendServletResponse(ServletResponse response, ErrorResponse errorResponse) throws IOException {
        byte[] responseToSend = getResponseInBytes(errorResponse);
        ((HttpServletResponse) response).setHeader("Content-Type", "application/json");
        ((HttpServletResponse) response).setStatus(403);
        response.getOutputStream().write(responseToSend);
    }

    private byte[] getResponseInBytes(ErrorResponse errorResponse) throws JsonProcessingException {
        String serialized = new ObjectMapper().writeValueAsString(errorResponse);
        return serialized.getBytes();
    }

    @Override
    public void destroy() {

    }
}
