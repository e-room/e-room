package com.project.Project.common.exception;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiErrorResult> conversionFailureHandler(ConversionFailedException ex) {

        String message = ex.getValue() + "는 " + ex.getTargetType() + "으로 변환할 수 없습니다.";
        String cause = ex.getClass().getName();

        return ResponseEntity.badRequest()
                .body(ApiErrorResult.builder().message(message).cause(cause).build());
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResult> MethodArgumentTypeHandler(MethodArgumentTypeMismatchException ex) {

        String message = "파라미터가 " + ex.getParameter().getParameterType() + " 타입이 아닙니다.";
        String cause = ex.getClass().getName();

        return ResponseEntity.badRequest()
                .body(ApiErrorResult.builder().message(message).cause(cause).build());
    }


    @ExceptionHandler
    public ResponseEntity<ApiErrorResult> ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        ConstraintViolation constraintViolation = e.getConstraintViolations().stream().collect(Collectors.toList()).get(0);
        String errorCodeString = constraintViolation.getMessageTemplate();
        ErrorCode errorCode = ErrorCode.valueOf(errorCodeString);
        String cause = e.getClass().getName();
        return ResponseEntity.badRequest()
                .body(ApiErrorResult.builder().errorCode(errorCode).message(errorCode.getMessage()).cause(cause).build());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getMessage();
        String cause = ex.getClass().getName();
        ApiErrorResult apiErrorResult = ApiErrorResult.builder().message(message).cause(cause).build();
        return super.handleExceptionInternal(ex, apiErrorResult, headers, status, request);
    }

    @ExceptionHandler(CustomException.class)
    public <T extends ApiErrorResult> ResponseEntity<T> customExceptionHandler(CustomException ex, WebRequest request) {

        ApiErrorResult apiErrorResult = ApiErrorResult.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .cause(ex.getClass().getName())
                .build();
        ResponseEntity response = super.handleExceptionInternal(ex, apiErrorResult, new HttpHeaders(), ex.getErrorCode().getStatus(), request);
        return response;
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResult> duplicatedEntryExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        return ResponseEntity.status(ex.getErrorCode())
                .body(ApiErrorResult.builder().message("중복된 값입니다.").cause(ex.getClass().toString()).build());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiErrorResult> fileSizeLimitExceptionHandler(MultipartException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResult.builder().message("파일 처리에 실패했습니다.").cause(ex.getClass().toString()).build());

    }

    @ExceptionHandler(Exception.class)
    public <T extends ApiErrorResult> ResponseEntity<T> generalExceptionHandler(Exception exception, WebRequest request) {
        ResponseEntity response = handleExceptionInternal(exception, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
        return response;
    }
}


