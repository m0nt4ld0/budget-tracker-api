package com.mmontaldo.budget_tracker.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.mmontaldo.budget_tracker.model.request.AuthRequestDto;
import com.mmontaldo.budget_tracker.model.response.AuthResponseDto;
import com.mmontaldo.budget_tracker.service.UsuarioService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService usuarioService;
    
    @PostMapping("/login")
    public AuthResponseDto getLogin(@RequestBody AuthRequestDto authRequestDto) {
        return usuarioService.getLogin(authRequestDto);
    }
}
