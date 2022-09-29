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
    public ResponseEntity<ApiErrorResult> MethodArgumentTypeHandler(MethodArgumentNotValidException ex){

        String message = "파라미터가 유효하지 않습니다.";
        String cause = ex.getClass().getName();

        return ResponseEntity.badRequest()
                .body(ApiErrorResult.builder().message(message).cause(cause).build());
    }

    @ExceptionHandler
    public ResponseEntity<JsonResult> handle (ConstraintViolationException e){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(HttpStatus.BAD_REQUEST);
        jsonResult.setMessage(e.getMessage());
        jsonResult.setData(e.getClass().toString());
        return ResponseEntity.badRequest().body(jsonResult);
    }
}


