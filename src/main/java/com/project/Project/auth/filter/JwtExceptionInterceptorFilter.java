package com.project.Project.auth.filter;

import com.project.Project.auth.exception.JwtAuthenticationException;
import com.project.Project.exception.ErrorCode;
import io.jsonwebtoken.JwtException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionInterceptorFilter extends ExceptionTranslationFilter {

    public JwtExceptionInterceptorFilter(AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationEntryPoint);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    //    @Override
    private void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException ex) {
            this.sendStartAuthentication(request, response, filterChain, ex);
        } catch (JwtException ex) {
            JwtAuthenticationException authenticationException = new JwtAuthenticationException(ErrorCode.JWT_SERVER_ERROR.getMessage(), ex, ErrorCode.JWT_SERVER_ERROR);
            this.sendStartAuthentication(request, response, filterChain, authenticationException);
        }
    }
}
