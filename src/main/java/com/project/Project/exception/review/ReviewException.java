package com.project.Project.exception.review;

import com.project.Project.exception.CustomException;
import com.project.Project.exception.ErrorCode;

public class ReviewException extends CustomException {
    public ReviewException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}