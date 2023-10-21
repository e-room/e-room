package com.project.Project.auth.handler;

import com.project.Project.auth.exception.BasicAuthException;
import com.project.Project.common.exception.ApiErrorResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BasicAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        BasicAuthException basicAuthException = (BasicAuthException) exception;
        response.setStatus(basicAuthException.getErrorCode().getStatus().value());
        response.setContentType("application/json");
        ApiErrorResult errorResponse = ApiErrorResult.builder()
                .cause(exception.getClass().getName())
                .message(basicAuthException.getMessage()).build();
        try {
            String json = errorResponse.toString();
            System.out.println(json);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
