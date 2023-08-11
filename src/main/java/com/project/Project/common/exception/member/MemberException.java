package com.project.Project.common.exception.member;


import com.project.Project.common.exception.CustomException;
import com.project.Project.common.exception.ErrorCode;

public class MemberException extends CustomException {
    public MemberException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
