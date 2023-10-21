package com.project.Project.common.exception.review;

import com.project.Project.common.exception.CustomException;
import com.project.Project.common.exception.ErrorCode;

public class ReviewImageException extends CustomException {
    public ReviewImageException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ReviewImageException(ErrorCode errorCode) {
        super(errorCode);
    }
}