package com.project.Project.exception.interaction;

import com.project.Project.exception.CustomException;
import com.project.Project.exception.ErrorCode;

public class FavoriteException extends CustomException {
    public FavoriteException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public FavoriteException(ErrorCode errorCode) {
        super(errorCode);
    }
}
