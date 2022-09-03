package com.project.Project.exception;

import com.project.Project.Util.JsonResult;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<JsonResult> conversionFailureHandler(ConversionFailedException ex){

        String message = ex.getValue() + "는 " + ex.getTargetType() + "으로 변환할 수 없습니다.";
        JsonResult jsonResult = new JsonResult(HttpStatus.BAD_REQUEST,message,ex.getClass().toString());

        return ResponseEntity.badRequest()
                .body(jsonResult);
    }

    @ExceptionHandler
    public ResponseEntity<JsonResult> MethodArgumentType(MethodArgumentTypeMismatchException ex){

        String message = "파라미터가 " + ex.getParameter() + " 타입이 아닙니다.";
        JsonResult jsonResult = new JsonResult(HttpStatus.BAD_REQUEST,message,ex.getClass().toString());

        return ResponseEntity.badRequest()
                .body(jsonResult);
    }

    @ExceptionHandler
    public ResponseEntity<JsonResult> MethodArgumentType(MethodArgumentNotValidException ex){

        String message = "파라미터가 유효하지 않습니다.";
        JsonResult jsonResult = new JsonResult(HttpStatus.BAD_REQUEST,ex.getMessage(),ex.getClass().toString());

        return ResponseEntity.badRequest()
                .body(jsonResult);
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
