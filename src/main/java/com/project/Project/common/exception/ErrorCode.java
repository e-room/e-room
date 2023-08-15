package com.project.Project.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자가 존재하지 않습니다."),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 이름의 역할이 없습니다."),


    //Auth
    JWT_BAD_REQUEST(HttpStatus.UNAUTHORIZED, "잘못된 JWT입니다."),
    JWT_ALL_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰들이 모두 만료되었습니다"),
    JWT_DENIED(HttpStatus.UNAUTHORIZED, "토큰 해독 중 발생하였습니다."),
    INVALID_BASIC_AUTH(HttpStatus.UNAUTHORIZED, "잘못된 userId입니다."),
    JWT_SERVER_ERROR(HttpStatus.UNAUTHORIZED, "jwt 에러가 발생했습니다."),

    //Building
    BUILDING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 건물이 존재하지 않습니다."),
    BUILDING_NPE(HttpStatus.INTERNAL_SERVER_ERROR, "건물 참조가 Null입니다"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "알맞은 도로명 주소가 없습니다."),
    ADDRESS_BAD_REQUEST(HttpStatus.BAD_REQUEST, "주소 값이 올바르지 않습니다."),
    COORDINATE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "좌표 값이 올바르지 않습니다."),

    // Interaction
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "찜한 자취방 목록에 해당 건물이 존재하지 않습니다."),
    FAVORITE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 찜한 방입니다."),

    //Review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 리뷰가 존재하지 않습니다."),
    REVIEW_DUPLICATED(HttpStatus.BAD_REQUEST, "하나의 건물에는 하나의 리뷰만 작성할 수 있습니다."),
    REVIEW_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "해당 리뷰에 접근할 수 없습니다."),

    //ReviewImage
    NO_REVIEW_IN_REVIEW_IMAGE(HttpStatus.BAD_REQUEST, "리뷰 이미지에 리뷰가 할당되어있지 않습니다."),
    REVIEW_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 리뷰 이미지가 없습니다"),

    //Checklist
    CHECKLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 체크리스트가 존재하지 않습니다."),
    CHECKLIST_IMAGE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지는 최대 5개까지 등록할 수 있습니다."),
    CHECKLIST_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 이미지가 없습니다"),
    CHECKLIST_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 체크리스트 질문지가 없습니다."),
    CHECKLIST_QUESTION_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "본인의 체크리스트 질문지만 수정할 수 있습니다."),

    //etc
    UUID_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 UUID가 존재하지 않습니다."),
    EVENT_LISTENER_INJECTION(HttpStatus.INTERNAL_SERVER_ERROR, "의존성 주입에 실패했습니다."),
    IMAGE_RESIZE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 변환에 실패했습니다"),
    IMAGE_ONLY(HttpStatus.BAD_REQUEST, "이미지만 썸네일로 생성 가능합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "문제가 생겼습니다. 잠시 후 다시 시도해주세요.");
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
