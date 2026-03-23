package com.mmontaldo.budget_tracker.service.impl;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.entity.UsuarioEntity;
import com.mmontaldo.budget_tracker.exception.EmailYaRegistradoException;
import com.mmontaldo.budget_tracker.exception.UsuarioNoAutorizadoException;
import com.mmontaldo.budget_tracker.exception.UsuarioYaRegistradoException;
import com.mmontaldo.budget_tracker.model.dto.UsuarioDto;
import com.mmontaldo.budget_tracker.model.dto.UsuarioUpdateDto;
import com.mmontaldo.budget_tracker.model.request.AuthRequestDto;
import com.mmontaldo.budget_tracker.model.response.AuthResponseDto;
import com.mmontaldo.budget_tracker.repository.UsuarioRepository;
import com.mmontaldo.budget_tracker.service.JwtService;
import com.mmontaldo.budget_tracker.service.UsuarioService;
import com.mmontaldo.budget_tracker.config.AuditConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuditConfig auditConfig;

    @Override
    public AuthResponseDto getLogin(AuthRequestDto authRequestDto) throws UsuarioNoAutorizadoException {
        UsuarioEntity usuario = usuarioRepository
            .findByUsuarioAndActivoTrue(authRequestDto.username())
            .orElseThrow(() -> new UsuarioNoAutorizadoException("Usuario no autorizado"));

        return new AuthResponseDto(
            usuario.getId(),
            usuario.getUsuario(),
            usuario.getEmail(),
            jwtService.generateToken(usuario.getUsuario()),
            usuario.getNombre(),
            usuario.getImagenUrl(),
            usuario.getActivo()
        );
    }

    @Override
    public UsuarioDto createUsuario(UsuarioDto usuarioDto)
            throws UsuarioYaRegistradoException, EmailYaRegistradoException {

        String auditUser = auditConfig.getEnabled()
                ? auditConfig.getDefaultUser()
                : "budget_tracker_api";

        if (usuarioRepository.findByUsuario(usuarioDto.getUsuario()).isPresent()) {
            throw new UsuarioYaRegistradoException("Ya existe un usuario con ese nombre.");
        }

        if (usuarioRepository.findByEmail(usuarioDto.getEmail()).isPresent()) {
            throw new EmailYaRegistradoException("Ese email ya fue registrado por otro usuario.");
        }

        usuarioDto.setActivo(true);

        UsuarioEntity usuario = usuarioRepository.save(UsuarioEntity.builder()
                .usuario(usuarioDto.getUsuario())
                .nombre(usuarioDto.getNombre())
                .imagenUrl(usuarioDto.getImagenUrl())
                .activo(usuarioDto.getActivo())
                .email(usuarioDto.getEmail())
                .audTsIns(OffsetDateTime.now())
                .audTsInsUser(auditUser)
                .build());

        return toDto(usuario);
    }

    public UsuarioDto updateUsuario(UsuarioUpdateDto dto) {
        UsuarioEntity usuario = getUsuarioAutenticado();

        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto.getImagenUrl() != null) usuario.setImagenUrl(dto.getImagenUrl());
        if (dto.getEmail() != null) usuario.setEmail(dto.getEmail());

        usuario.setAudTsUpd(OffsetDateTime.now());
        usuario.setAudTsUpdUser(auditConfig.getDefaultUser());

        return toDto(usuarioRepository.save(usuario));
    }

    private UsuarioEntity getUsuarioAutenticado() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository
                .findByUsuario(username)
                .orElseThrow(() -> new UsuarioNoAutorizadoException("Usuario no encontrado"));
    }

    private UsuarioDto toDto(UsuarioEntity usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .usuario(usuario.getUsuario())
                .nombre(usuario.getNombre())
                .imagenUrl(usuario.getImagenUrl())
                .activo(usuario.getActivo())
                .email(usuario.getEmail())
                .build();
    }
}