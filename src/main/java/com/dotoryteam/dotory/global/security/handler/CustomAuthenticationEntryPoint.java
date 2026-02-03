package com.dotoryteam.dotory.global.security.handler;

import com.dotoryteam.dotory.global.common.dto.ApiResponse;

import com.dotoryteam.dotory.global.common.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object exception = request.getAttribute("exception");

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = "인증에 실패했습니다.";
        if (exception instanceof ApiException apiException) {
            status = apiException.status;
            message = apiException.getMessage();
        }

        sendResponse(response , status , message);
    }

    private void sendResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status.value());

        ApiResponse<Object> apiResponse = ApiResponse.failedOf(status , message).getBody();
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.getWriter().write(jsonResponse);
    }
}
