package com.project.Project.common.exception.member;

import com.project.Project.common.exception.CustomException;
import com.project.Project.common.exception.ErrorCode;

public class RoleException extends CustomException {

    public RoleException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public RoleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
