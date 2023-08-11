package com.project.Project.auth.exception;

import com.project.Project.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

    public ErrorCode errorCode;

    public JwtAuthenticationException(String msg) {
        super(msg);
        this.errorCode = ErrorCode.JWT_BAD_REQUEST;
    }

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public JwtAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }


    public JwtAuthenticationException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public JwtAuthenticationException(String msg, Throwable cause, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }


}
