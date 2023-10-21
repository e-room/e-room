package com.project.Project.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private ErrorCode errorCode;

    public CustomException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
