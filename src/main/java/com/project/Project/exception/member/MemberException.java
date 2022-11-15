package com.project.Project.exception.member;


import com.project.Project.exception.CustomException;
import com.project.Project.exception.ErrorCode;

public class MemberException extends CustomException {
    public MemberException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
