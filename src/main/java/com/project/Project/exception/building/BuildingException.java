package com.project.Project.exception.building;

import com.project.Project.exception.ErrorCode;

public class BuildingException extends RuntimeException{
    private ErrorCode errorCode;

    public BuildingException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public BuildingException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
