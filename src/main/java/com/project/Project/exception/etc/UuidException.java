package com.project.Project.exception.etc;

import com.project.Project.exception.CustomException;
import com.project.Project.exception.ErrorCode;

public class UuidException extends CustomException {
    public UuidException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public UuidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
