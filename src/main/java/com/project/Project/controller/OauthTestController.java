package com.project.Project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Controller
public class OauthTestController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public String oauthTest(Model model) {
        return "oauth-test";
    }



}
