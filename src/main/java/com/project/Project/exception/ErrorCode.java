package com.project.Project.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자가 존재하지 않습니다."),


    //Auth
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "잘못된 JWT입니다."),
    INVALID_BASIC_AUTH(HttpStatus.UNAUTHORIZED, "잘못된 userId입니다."),

    //Building
    BUILDING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 건물이 존재하지 않습니다."),
    BUILDING_NPE(HttpStatus.INTERNAL_SERVER_ERROR, "건물 참조가 Null입니다"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "알맞은 도로명 주소가 없습니다."),
    ADDRESS_BAD_REQUEST(HttpStatus.BAD_REQUEST, "주소 값이 올바르지 않습니다."),
    COORDINATE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "좌표 값이 올바르지 않습니다."),

    //Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 방이 존재하지 않습니다."),
    ROOM_NPE(HttpStatus.INTERNAL_SERVER_ERROR, "방 참조가 Null입니다"),

    // Interaction
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "찜한 자취방 목록에 해당 건물이 존재하지 않습니다."),
    FAVORITE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 찜한 방입니다."),

    //Review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 리뷰가 존재하지 않습니다."),

    //etc
    UUID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 UUID가 존재하지 않습니다."),
    EVENT_LISTENER_INJECTION(HttpStatus.INTERNAL_SERVER_ERROR, "의존성 주입에 실패했습니다."),
    IMAGE_RESIZE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 변환에 실패했습니다"),
    IMAGE_ONLY(HttpStatus.BAD_REQUEST, "이미지만 썸네일로 생성 가능합니다.");
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
