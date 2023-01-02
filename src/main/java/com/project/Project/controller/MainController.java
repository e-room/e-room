package com.project.Project.controller;

import com.project.Project.exception.ApiErrorResult;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MainController {

    @GetMapping("/health")
    public String oauthTest() {
        return "I'm healthy";
    }

    @GetMapping("/exception-test")
    public ResponseEntity<ApiErrorResult> exceptionTest() {
        throw new BuildingException("test", ErrorCode.BUILDING_NPE);
    }

    @GetMapping("/test")
    public String test() {
        return "I'm test";
    }

    @GetMapping("/internal-exception")
    public ResponseEntity<ApiErrorResult> internalExceptionTest() {
        throw new RuntimeException("test");
    }
}
