package com.project.Project.auth.handler;

import com.project.Project.auth.exception.JwtException;
import com.project.Project.exception.ApiErrorResult;
import com.project.Project.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //response에 넣기
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ErrorCode errorCode = null;
        if (authException instanceof JwtException) {
            errorCode = ((JwtException) authException).getErrorCode();
        }

        ApiErrorResult errorResponse = ApiErrorResult.builder()
                .errorCode(errorCode)
                .cause(authException.getClass().getName())
                .message(authException.getMessage()).build();
        try {
            String json = errorResponse.toString();
            System.out.println(json);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
