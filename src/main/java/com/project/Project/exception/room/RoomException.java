package com.project.Project.exception.room;

import com.project.Project.exception.CustomException;
import com.project.Project.exception.ErrorCode;

public class RoomException extends CustomException {


    public RoomException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public RoomException(ErrorCode errorCode) {
        super(errorCode);
    }
}
