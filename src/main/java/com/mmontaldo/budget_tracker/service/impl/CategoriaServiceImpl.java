package com.mmontaldo.budget_tracker.service.impl;

import java.util.List;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.config.AuditConfig;
import com.mmontaldo.budget_tracker.entity.CategoriaEntity;
import com.mmontaldo.budget_tracker.exception.CategoriaNotFoundException;
import com.mmontaldo.budget_tracker.model.TipoMovimiento;
import com.mmontaldo.budget_tracker.model.dto.CategoriaDto;
import com.mmontaldo.budget_tracker.repository.CategoriaRepository;
import com.mmontaldo.budget_tracker.service.CategoriaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final AuditConfig auditConfig;

    @Override
    public List<CategoriaDto> getCategorias() {
        return categoriaRepository.findByActivoTrue()
                .stream()
                .map(entity -> CategoriaDto.builder()
                        .id(entity.getId())
                        .categoria(entity.getCategoria())
                        .icono(entity.getIcono())
                        .build())
                .toList();
    }

    @Override
    public List<CategoriaDto> getCategoriasIngresos() {
        return categoriaRepository.findByActivoTrue()
                .stream()
                .map(entity -> CategoriaDto.builder()
                        .id(entity.getId())
                        .categoria(entity.getCategoria())
                        .tipoMovimiento(TipoMovimiento.INGRESO.name())
                        .icono(entity.getIcono())
                        .build())
                .toList();
    }

    @Override
    public List<CategoriaDto> getCategoriasGastos() {
        return categoriaRepository.findByActivoTrue()
                .stream()
                .map(entity -> CategoriaDto.builder()
                        .id(entity.getId())
                        .categoria(entity.getCategoria())
                        .tipoMovimiento(TipoMovimiento.EGRESO.name())
                        .icono(entity.getIcono())
                        .build())
                .toList();
    }

    @Override
    public List<CategoriaDto> getCategoriasMovimientos() {
        return categoriaRepository.findByActivoTrue()
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

        dto.setTipoMovimiento(TipoMovimiento.EGRESO.name());

        String auditUser = auditConfig.getEnabled()
                ? auditConfig.getDefaultUser()
                : null;

        CategoriaEntity entity = CategoriaEntity.builder()
                .categoria(dto.getCategoria())
                .icono(dto.getIcono())
                .activo(true)
                .audTsIns(OffsetDateTime.now())
                .audTsInsUser(auditUser)
                .build();

        CategoriaEntity guardada = categoriaRepository.save(entity);
        return CategoriaDto.builder()
            .id(guardada.getId())
            .categoria(guardada.getCategoria())
            .icono(guardada.getIcono())
            .build();
    }
        
    @Override
    @Transactional
    public CategoriaDto crearCategoriaIngreso(CategoriaDto dto) {

        dto.setTipoMovimiento(TipoMovimiento.INGRESO.name());
        
        String auditUser = auditConfig.getEnabled()
                ? auditConfig.getDefaultUser()
                : null;

        CategoriaEntity entity = CategoriaEntity.builder()
                .categoria(dto.getCategoria())
                .icono(dto.getIcono())
                .activo(true)
                .audTsIns(OffsetDateTime.now())
                .audTsInsUser(auditUser)
                .build();

        CategoriaEntity guardada = categoriaRepository.save(entity);
        return CategoriaDto.builder()
            .id(guardada.getId())
            .categoria(guardada.getCategoria())
            .icono(guardada.getIcono())
            .build();
    }

    public CategoriaDto editarCategoria(Long id, CategoriaDto dto) throws CategoriaNotFoundException{
        log.info("Editando categoria {id={}}", id);
        CategoriaEntity categoriaEntity = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria no encontrada"));
        categoriaEntity.setCategoria(dto.getCategoria());
        categoriaEntity.setIcono(dto.getIcono());
        CategoriaEntity guardada = categoriaRepository.save(categoriaEntity);
        return CategoriaDto.builder()
                .id(guardada.getId())
                .categoria(guardada.getCategoria())
                .icono(guardada.getIcono())
                .build();
    }

    public void eliminarCategoria(Long id) throws CategoriaNotFoundException {
        log.info("Eliminando categoria {id={}}", id);
        
        CategoriaEntity categoriaEntity = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria no encontrada"));
        
        // Baja lógica
        categoriaEntity.setActivo(false);

        categoriaRepository.save(categoriaEntity);
    }
}

