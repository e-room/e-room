package com.project.Project.exception;

import com.project.Project.Util.JsonResult;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

//    @ExceptionHandler
//    public ResponseEntity<JsonResult> conversionFailureHandler(ConversionFailedException ex){
//
//        JsonResult jsonResult = JsonResult.builder()
//                .code(HttpStatus.BAD_REQUEST)
//                .data(ex)
//                .message(ex.getValue() + "는 " + ex.getTargetType() + "으로 변환할 수 없습니다.").build();
//
//        return ResponseEntity.badRequest()
//                .body(jsonResult);
//    }

    @ExceptionHandler
    public ResponseEntity<JsonResult> handle (ConstraintViolationException e){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(HttpStatus.BAD_REQUEST);
        jsonResult.setMessage(e.getMessage());
        jsonResult.setData(e.getClass().toString());
        return new ResponseEntity<>(jsonResult,HttpStatus.BAD_REQUEST);
    }
}
