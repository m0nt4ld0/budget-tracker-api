package com.mmontaldo.budget_tracker.service.impl;

import java.util.List;
import java.time.OffsetDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.config.AuditConfig;
import com.mmontaldo.budget_tracker.entity.CategoriaEntity;
import com.mmontaldo.budget_tracker.entity.UsuarioEntity;
import com.mmontaldo.budget_tracker.exception.CategoriaNotFoundException;
import com.mmontaldo.budget_tracker.exception.UsuarioNoAutorizadoException;
import com.mmontaldo.budget_tracker.model.TipoMovimiento;
import com.mmontaldo.budget_tracker.model.dto.CategoriaDto;
import com.mmontaldo.budget_tracker.repository.CategoriaRepository;
import com.mmontaldo.budget_tracker.repository.UsuarioRepository;
import com.mmontaldo.budget_tracker.service.CategoriaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditConfig auditConfig;

    @Override
    public List<CategoriaDto> getCategorias() {
        UsuarioEntity usuario = getUsuarioAutenticado();

        return categoriaRepository.findByUsuarioAndActivoTrue(usuario)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<CategoriaDto> getCategoriasIngresos() {
        UsuarioEntity usuario = getUsuarioAutenticado();

        return categoriaRepository.findByUsuarioAndActivoTrue(usuario)
                .stream()
                .map(entity -> toDtoWithTipo(entity, TipoMovimiento.INGRESO))
                .toList();
    }

    @Override
    public List<CategoriaDto> getCategoriasGastos() {
        UsuarioEntity usuario = getUsuarioAutenticado();

        return categoriaRepository.findByUsuarioAndActivoTrue(usuario)
                .stream()
                .map(entity -> toDtoWithTipo(entity, TipoMovimiento.EGRESO))
                .toList();
    }

    @Override
    public List<CategoriaDto> getCategoriasMovimientos() {
        UsuarioEntity usuario = getUsuarioAutenticado();

        return categoriaRepository.findByUsuarioAndActivoTrue(usuario)
                .stream()
                .map(entity -> CategoriaDto.builder()
                        .id(entity.getId())
                        .categoria(entity.getCategoria())
                        .tipoMovimiento(entity.getTipoMovimiento().name())
                        .icono(entity.getIcono())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public CategoriaDto crearCategoriaGasto(CategoriaDto dto) {

        UsuarioEntity usuario = getUsuarioAutenticado();

        String auditUser = auditConfig.getEnabled()
                ? auditConfig.getDefaultUser()
                : null;

        CategoriaEntity entity = CategoriaEntity.builder()
                .categoria(dto.getCategoria())
                .icono(dto.getIcono())
                .tipoMovimiento(TipoMovimiento.EGRESO)
                .activo(true)
                .usuario(usuario)
                .audTsIns(OffsetDateTime.now())
                .audTsInsUser(auditUser)
                .build();

        return toDto(categoriaRepository.save(entity));
    }

    @Override
    @Transactional
    public CategoriaDto crearCategoriaIngreso(CategoriaDto dto) {

        UsuarioEntity usuario = getUsuarioAutenticado();

        String auditUser = auditConfig.getEnabled()
                ? auditConfig.getDefaultUser()
                : null;

        CategoriaEntity entity = CategoriaEntity.builder()
                .categoria(dto.getCategoria())
                .icono(dto.getIcono())
                .tipoMovimiento(TipoMovimiento.INGRESO)
                .activo(true)
                .usuario(usuario)
                .audTsIns(OffsetDateTime.now())
                .audTsInsUser(auditUser)
                .build();

        return toDto(categoriaRepository.save(entity));
    }

    @Override
    @Transactional
    public CategoriaDto editarCategoria(Long id, CategoriaDto dto) {
        log.info("Editando categoria {id={}}", id);

        UsuarioEntity usuario = getUsuarioAutenticado();

        CategoriaEntity categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria no encontrada"));

        if (!categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new UsuarioNoAutorizadoException("No podés editar esta categoría");
        }

        categoria.setCategoria(dto.getCategoria());
        categoria.setIcono(dto.getIcono());

        return toDto(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional
    public void eliminarCategoria(Long id) {
        log.info("Eliminando categoria {id={}}", id);

        UsuarioEntity usuario = getUsuarioAutenticado();

        CategoriaEntity categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria no encontrada"));

        if (!categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new UsuarioNoAutorizadoException("No podés eliminar esta categoría");
        }

        categoria.setActivo(false);

        categoriaRepository.save(categoria);
    }

    private CategoriaDto toDto(CategoriaEntity entity) {
        return CategoriaDto.builder()
                .id(entity.getId())
                .categoria(entity.getCategoria())
                .icono(entity.getIcono())
                .build();
    }

    private CategoriaDto toDtoWithTipo(CategoriaEntity entity, TipoMovimiento tipo) {
        return CategoriaDto.builder()
                .id(entity.getId())
                .categoria(entity.getCategoria())
                .tipoMovimiento(tipo.name())
                .icono(entity.getIcono())
                .build();
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
}