package com.mmontaldo.budget_tracker.service.impl;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.entity.UsuarioEntity;
import com.mmontaldo.budget_tracker.exception.EmailYaRegistradoException;
import com.mmontaldo.budget_tracker.exception.UsuarioNoAutorizadoException;
import com.mmontaldo.budget_tracker.exception.UsuarioYaRegistradoException;
import com.mmontaldo.budget_tracker.model.dto.UsuarioDto;
import com.mmontaldo.budget_tracker.model.request.AuthRequestDto;
import com.mmontaldo.budget_tracker.model.response.AuthResponseDto;
import com.mmontaldo.budget_tracker.repository.UsuarioRepository;
import com.mmontaldo.budget_tracker.service.JwtService;
import com.mmontaldo.budget_tracker.service.UsuarioService;
import com.mmontaldo.budget_tracker.config.AuditConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
            jwtService.generateToken(usuario.getUsuario()),
            usuario.getNombre(),
            usuario.getImagenUrl(),
            usuario.getActivo()
        );
    }
    
    @Override
    public UsuarioDto createUsuario(UsuarioDto usuarioDto) throws UsuarioYaRegistradoException, EmailYaRegistradoException {
        
        String auditUser = auditConfig.getEnabled() ? auditConfig.getDefaultUser() : "budget_tracker_api";

        UsuarioEntity usuarioExistente = usuarioRepository.findByUsuario(usuarioDto.getUsuario()).orElse(null);
        if (usuarioExistente != null) {
            throw new UsuarioYaRegistradoException("Ya existe un usuario con ese nombre.");
        }

        UsuarioEntity emailExistente = usuarioRepository.findByEmail(usuarioDto.getEmail()).orElse(null);
        if (emailExistente != null) {
            throw new EmailYaRegistradoException("Ese email ya fue registrado por otro usuario.");
        }

        // TODO: Implementar activación de la cuenta por mail, y setear activo en false al crear
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
        return UsuarioDto.builder()
            .id(usuario.getId())
            .usuario(usuario.getUsuario())
            .nombre(usuario.getNombre())
            .imagenUrl(usuario.getImagenUrl())
            .activo(usuario.getActivo())
            .email(usuario.getEmail())
            .build();
    }

    public UsuarioDto updateUsuario(UsuarioDto usuarioDto) throws UsuarioNoAutorizadoException {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioDto.getId())
            .orElseThrow(() -> new UsuarioNoAutorizadoException("Usuario no encontrado"));

        if (usuarioDto.getNombre() != null)  usuario.setNombre(usuarioDto.getNombre());
        if (usuarioDto.getImagenUrl() != null) usuario.setImagenUrl(usuarioDto.getImagenUrl());

        // ToDo: Cuando implemente envio de email de confirmacion, hacer otro endpoint para el cambio del correo
        if (usuarioDto.getEmail() != null) usuario.setEmail(usuarioDto.getEmail());

        usuario.setAudTsUpd(OffsetDateTime.now());
        usuario.setAudTsUpdUser(auditConfig.getDefaultUser());

        usuarioRepository.save(usuario);

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
