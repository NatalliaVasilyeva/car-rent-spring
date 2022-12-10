package com.dmdev.api.controller;

import com.dmdev.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WelcomeApi {

    private final CarService carService;

    @GetMapping({"", "welcome"})
    public String getWelcomePage(Model model) {
        carService.getAllAvailable().stream()
                        .forEach(System.out::println);
        model.addAttribute("cars", carService.getAllAvailable());
        return "welcome/init";
    }
}