package com.project.Project.common.exception.interaction;

import com.project.Project.common.exception.CustomException;
import com.project.Project.common.exception.ErrorCode;

public class FavoriteException extends CustomException {
    public FavoriteException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public FavoriteException(ErrorCode errorCode) {
        super(errorCode);
    }
}
