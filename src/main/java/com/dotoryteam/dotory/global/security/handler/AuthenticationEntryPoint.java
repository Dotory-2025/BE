package com.dotoryteam.dotory.global.security.handler;

import com.dotoryteam.dotory.global.common.dto.ApiResponse;
import com.dotoryteam.dotory.global.security.enums.ErrorCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object exception = request.getAttribute("exception");

        ErrorCode errorCode;

        if (exception instanceof ErrorCode) {
            errorCode = (ErrorCode) exception;
        } else {
            errorCode = ErrorCode.UNAUTHORIZED;
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<Object> apiResponse = ApiResponse.of(errorCode.getMessage());

        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        //{"message": 메시지 , "data": null}
        response.getWriter().write(jsonResponse);
    }
}
