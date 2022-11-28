package com.dmdev.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginApi {

    @GetMapping("/login")
    public String getLogin() {
        return "layout/user/login";
    }
}