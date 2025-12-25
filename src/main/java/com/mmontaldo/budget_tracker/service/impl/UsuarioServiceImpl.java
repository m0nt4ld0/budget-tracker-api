package com.mmontaldo.budget_tracker.service.impl;

import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.entity.UsuarioEntity;
import com.mmontaldo.budget_tracker.exception.UsuarioNoAutorizadoException;
import com.mmontaldo.budget_tracker.model.request.AuthRequestDto;
import com.mmontaldo.budget_tracker.model.response.AuthResponseDto;
import com.mmontaldo.budget_tracker.repository.UsuarioRepository;
import com.mmontaldo.budget_tracker.service.JwtService;
import com.mmontaldo.budget_tracker.service.UsuarioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @Override
    public AuthResponseDto getLogin(AuthRequestDto authRequestDto) throws UsuarioNoAutorizadoException {
        UsuarioEntity usuario = usuarioRepository
            .findByUsuarioAndActivoTrue(authRequestDto.username())
            .orElseThrow(() -> new UsuarioNoAutorizadoException("Usuario no autorizado"));

        return new AuthResponseDto(
            usuario.getUsuario(),
            jwtService.generateToken(usuario.getUsuario()),
            usuario.getNombre(),
            usuario.getImagenUrl(),
            usuario.getActivo()
        );
    }
}
