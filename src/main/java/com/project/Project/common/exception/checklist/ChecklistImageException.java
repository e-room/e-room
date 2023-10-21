package com.project.Project.common.exception.checklist;

import com.project.Project.common.exception.CustomException;
import com.project.Project.common.exception.ErrorCode;

public class ChecklistImageException extends CustomException {
    public ChecklistImageException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ChecklistImageException(ErrorCode errorCode) {
        super(errorCode);
    }
}