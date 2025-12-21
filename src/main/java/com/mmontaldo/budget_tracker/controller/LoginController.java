package com.mmontaldo.budget_tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login/")
public class LoginController {
    
    @GetMapping
    public String getLogin() {
        return "login";
    }
}
