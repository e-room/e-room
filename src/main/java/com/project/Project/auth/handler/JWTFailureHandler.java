package com.project.Project.auth.handler;

import com.project.Project.auth.exception.JwtException;
import com.project.Project.exception.ApiErrorResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        JwtException jwtException = (JwtException) exception;
        response.setStatus(jwtException.getErrorCode().getStatus().value());
        response.setContentType("application/json");
        ApiErrorResult errorResponse = ApiErrorResult.builder()
                .cause(exception.getClass().getName())
                .message(jwtException.getMessage()).build();
        try {
            String json = errorResponse.toString();
            System.out.println(json);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
