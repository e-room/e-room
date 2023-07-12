package com.project.Project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Oauth Test API", description = "테스트용 API (호출X)")
@RequiredArgsConstructor
@Controller
public class OauthTestController {

    @GetMapping("/login")
    public String oauthTest(Model model, @AuthenticationPrincipal OAuth2AuthenticationToken auth) {
        return "oauth-test";
    }
}
