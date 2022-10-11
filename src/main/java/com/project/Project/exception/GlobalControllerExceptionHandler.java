package com.project.Project.exception;

import com.amazonaws.Response;
import com.project.Project.Util.JsonResult;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiErrorResult> conversionFailureHandler(ConversionFailedException ex){

        String message = ex.getValue() + "는 " + ex.getTargetType() + "으로 변환할 수 없습니다.";
        String cause = ex.getClass().getName();

        return ResponseEntity.badRequest()
                .body(ApiErrorResult.builder().message(message).cause(cause).build());
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResult> MethodArgumentTypeHandler(MethodArgumentTypeMismatchException ex){

        String message = "파라미터가 " + ex.getParameter() + " 타입이 아닙니다.";
        String cause = ex.getClass().getName();

        return ResponseEntity.badRequest()
                .body(ApiErrorResult.builder().message(message).cause(cause).build());
    }


    @ExceptionHandler
    public ResponseEntity<ApiErrorResult> ConstraintViolationExceptionHandler (ConstraintViolationException e){
        String message = "request가 Validate 하지 않습니다.";
        String cause = e.getClass().getName();
        return ResponseEntity.badRequest()
                .body(ApiErrorResult.builder().message(message).cause(cause).build());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ((ErrorCode) body).getMessage();
        String cause = ex.getClass().getName();
        ApiErrorResult apiErrorResult = ApiErrorResult.builder().message(message).cause(cause).build();
        return super.handleExceptionInternal(ex, apiErrorResult, headers, status, request);
    }

    @ExceptionHandler(CustomException.class)
    private <T extends ApiErrorResult> ResponseEntity<T> CustomExceptionHandler(CustomException ex, WebRequest request) {
        ResponseEntity response = handleExceptionInternal(ex, null,null,ex.getErrorCode().getStatus(), request);
        return response;
    }
}


