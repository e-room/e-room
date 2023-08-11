package com.project.Project.common.exception.review;

import com.project.Project.common.exception.CustomException;
import com.project.Project.common.exception.ErrorCode;

public class ReviewException extends CustomException {
    public ReviewException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}