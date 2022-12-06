package com.project.Project.controller;

import com.project.Project.auth.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Controller
public class OauthTestController {

    private final HttpSession httpSession;

    @GetMapping("/login")
    public String oauthTest(Model model, @AuthenticationPrincipal OAuth2AuthenticationToken auth) {
        return "oauth-test";
    }

    @GetMapping("/api/profile")
    @ResponseBody
    public ResponseEntity<Object> getProfile(@AuthenticationPrincipal MemberDto auth) {
        //??
        // auth가 왜 null이지...
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getPrincipal();
        return ResponseEntity.ok(authentication.getPrincipal());
    }
}
