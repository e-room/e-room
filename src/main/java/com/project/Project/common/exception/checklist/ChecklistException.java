package com.project.Project.common.exception.checklist;

import com.project.Project.common.exception.CustomException;
import com.project.Project.common.exception.ErrorCode;

public class ChecklistException extends CustomException {
    public ChecklistException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ChecklistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
