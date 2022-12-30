package com.project.Project.controller;

import com.project.Project.exception.ApiErrorResult;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class MainController {

    @GetMapping("/health")
    public String oauthTest(@RequestParam("test") MultipartFile test) {
        return "I'm healthy";
    }

    @GetMapping("/exception-test")
    public ResponseEntity<ApiErrorResult> exceptionTest() {
        throw new BuildingException("test", ErrorCode.BUILDING_NPE);
    }


    @GetMapping("/internal-exception")
    public ResponseEntity<ApiErrorResult> internalExceptionTest() {
        throw new RuntimeException("test");
    }
}
