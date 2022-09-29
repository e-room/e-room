package com.project.Project.exception.building;

import com.project.Project.exception.CustomException;
import com.project.Project.exception.ErrorCode;

public class BuildingException extends CustomException {
    public BuildingException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public BuildingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
