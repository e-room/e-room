package com.project.Project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MainController {

    @GetMapping("/health")
    public String oauthTest(Model model, @AuthenticationPrincipal OAuth2AuthenticationToken auth) {
        return "I'm healthy";
    }
}
