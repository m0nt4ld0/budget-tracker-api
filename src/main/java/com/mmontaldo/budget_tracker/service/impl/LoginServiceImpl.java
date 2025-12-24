package com.mmontaldo.budget_tracker.service.impl;

import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.service.JwtService;
import com.mmontaldo.budget_tracker.service.LoginService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final JwtService jwtService;
    
    public String login(String username) {
        return jwtService.generateToken(username);
    }
}
    
