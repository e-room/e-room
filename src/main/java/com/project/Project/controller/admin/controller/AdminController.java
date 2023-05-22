package com.project.Project.controller.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("")
    public String adminLogin() {
        return "/admin/login";
    }

    @GetMapping("/home")
    public String adminHome() {
        return "/admin/home";
    }
}
