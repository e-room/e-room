package com.project.Project.common.exception.etc;

import com.project.Project.common.exception.CustomException;
import com.project.Project.common.exception.ErrorCode;

public class UuidException extends CustomException {
    public UuidException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public UuidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
