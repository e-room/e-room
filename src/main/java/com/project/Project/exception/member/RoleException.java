package com.project.Project.exception.member;

import com.project.Project.exception.CustomException;
import com.project.Project.exception.ErrorCode;

public class RoleException extends CustomException {

    public RoleException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public RoleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
