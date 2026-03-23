package com.mmontaldo.budget_tracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mmontaldo.budget_tracker.model.dto.UsuarioDto;
import com.mmontaldo.budget_tracker.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto register(@RequestBody UsuarioDto usuarioDto) {
        return usuarioService.createUsuario(usuarioDto);
    }

    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioDto updateUsuario(@RequestBody UsuarioDto usuarioDto) {
        return usuarioService.updateUsuario(usuarioDto);
    }
}
