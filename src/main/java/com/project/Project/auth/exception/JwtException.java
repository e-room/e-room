package com.project.Project.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtException extends AuthenticationException {

    public JwtException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtException(String msg) {
        super(msg);
    }
}
