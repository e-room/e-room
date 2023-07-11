package com.project.Project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health API", description = "헬스 체크")
@RequiredArgsConstructor
@RestController
public class MainController {

    @GetMapping("/health")
    public String oauthTest() {
        return "I'm healthy";
    }

}
