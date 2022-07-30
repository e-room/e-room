package com.project.Project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Controller
public class ViewTestController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public String oauthTest(Model model) {
        return "login";
    }

    @GetMapping("/error")
    public String errorPage(Model model) {
        return "error";
    }



}
