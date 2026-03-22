package com.mmontaldo.budget_tracker.service;

import com.mmontaldo.budget_tracker.exception.UsuarioNoAutorizadoException;
import com.mmontaldo.budget_tracker.model.dto.UsuarioDto;
import com.mmontaldo.budget_tracker.model.request.AuthRequestDto;
import com.mmontaldo.budget_tracker.model.response.AuthResponseDto;

public interface UsuarioService {
    public AuthResponseDto getLogin(AuthRequestDto authRequestDto) throws UsuarioNoAutorizadoException;
    public UsuarioDto createUsuario(UsuarioDto usuarioDto);
    public UsuarioDto updateUsuario(UsuarioDto usuarioDto);
}
