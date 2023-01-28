package com.project.Project.auth.exception;

import com.project.Project.exception.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenctionException extends AuthenticationException {

    public ErrorCode errorCode;

    public JwtAuthenctionException(String msg) {
        super(msg);
        this.errorCode = ErrorCode.JWT_BAD_REQUEST;
    }

    public JwtAuthenctionException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public JwtAuthenctionException(String msg, Throwable cause) {
        super(msg, cause);
    }


    public JwtAuthenctionException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public JwtAuthenctionException(String msg, Throwable cause, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }


}
