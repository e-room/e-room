package com.project.Project.controller;

import com.project.Project.config.auth.dto.LoginUser;
import com.project.Project.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class OauthTestController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public String oauthTest(Model model, @LoginUser SessionUser user) {

        if (user != null) {
            model.addAttribute("loginUserName", user.getName());
        }

        return "oauth-test";
    }

//    @GetMapping("/login/oauth/google")
//    public String oauthRedirect(Model model) {
//        return "google-success";
//    }
}
