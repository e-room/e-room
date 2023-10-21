package com.project.Project.common.exception.building;

import com.project.Project.common.exception.CustomException;
import com.project.Project.common.exception.ErrorCode;

public class BuildingException extends CustomException {
    public BuildingException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public BuildingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
