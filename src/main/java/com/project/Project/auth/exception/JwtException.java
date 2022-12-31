package com.project.Project.auth.exception;

import com.project.Project.exception.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtException extends AuthenticationException {

    public ErrorCode errorCode;

    public JwtException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtException(String msg) {
        super(msg);
        this.errorCode = ErrorCode.INVALID_JWT;
    }

    public JwtException(String msg, Throwable cause, ErrorCode errorCode) {
        super(msg, cause);
        this.errorCode = errorCode;
    }


    public JwtException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
