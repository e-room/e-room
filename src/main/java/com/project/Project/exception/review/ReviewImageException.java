package com.project.Project.exception.review;

import com.project.Project.exception.CustomException;
import com.project.Project.exception.ErrorCode;

public class ReviewImageException extends CustomException {
    public ReviewImageException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ReviewImageException(ErrorCode errorCode) {
        super(errorCode);
    }
}