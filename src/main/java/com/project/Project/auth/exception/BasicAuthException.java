package com.project.Project.auth.exception;

import com.project.Project.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class BasicAuthException extends AuthenticationException {

    public ErrorCode errorCode;

    public BasicAuthException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BasicAuthException(String msg) {
        super(msg);
    }

    public BasicAuthException(String msg, Throwable cause, ErrorCode errorCode) {
        super(msg, cause);
        this.errorCode = errorCode;
    }


    public BasicAuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
