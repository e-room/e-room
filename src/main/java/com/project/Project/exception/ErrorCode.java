package com.project.Project.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    //Building
    BUILDING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 건물이 존재하지 않습니다."),
    BUILDING_NPE(HttpStatus.INTERNAL_SERVER_ERROR, "건물 참조가 Null입니다"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "알맞은 도로명 주소가 없습니다."),
    ADDRESS_BAD_REQUEST(HttpStatus.BAD_REQUEST, "주소 값이 올바르지 않습니다."),
    COORDINATE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "좌표 값이 올바르지 않습니다."),

    //Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 방이 존재하지 않습니다."),
    ROOM_NPE(HttpStatus.INTERNAL_SERVER_ERROR, "방 참조가 Null입니다"),

    //etc
    EVENT_LISTENER_INJECTION(HttpStatus.INTERNAL_SERVER_ERROR, "의존성 주입에 실패했습니다.");
    private final String message;
    private final HttpStatus status;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
