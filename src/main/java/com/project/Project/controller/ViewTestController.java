package com.project.Project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;


// 아래의 접속 url들 같은 경우 다시 재설계 예정
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

    @GetMapping("/review/map")
    public String reviewMap() {
        return "review-map";
    }

    @GetMapping("/review/list")
    public String reviewList() {
        return "review-list";
    }

    @GetMapping("/favorite/list")
    public String favoriteList() { return "favorite-list"; }
}
