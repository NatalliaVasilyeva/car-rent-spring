package com.dmdev.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/")
public class WelcomeApi {

    @GetMapping("welcome")
    public String getWelcomePage(Model model) {
        return "welcome/init";
    }
}