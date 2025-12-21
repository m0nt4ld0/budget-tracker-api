package com.mmontaldo.budget_tracker.service;

import java.util.List;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.config.AuditConfig;
import com.mmontaldo.budget_tracker.entity.CategoriaEntity;
import com.mmontaldo.budget_tracker.model.dto.CategoriaDto;
import com.mmontaldo.budget_tracker.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final AuditConfig auditConfig;

    public List<CategoriaDto> getCategorias() {
        return categoriaRepository.findByActivoTrue()
                .stream()
                .map(entity -> CategoriaDto.builder()
                        .id(entity.getId())
                        .categoria(entity.getCategoria())
                        .build())
                .toList();
    }

    public CategoriaDto crearCategoria(CategoriaDto dto) {

        String auditUser = auditConfig.getEnabled()
                ? auditConfig.getDefaultUser()
                : null;

        CategoriaEntity entity = CategoriaEntity.builder()
                .categoria(dto.getCategoria())
                .activo(true)
                .audTsIns(OffsetDateTime.now())
                .audTsInsUser(auditUser)
                .build();

        CategoriaEntity guardada = categoriaRepository.save(entity);
        return CategoriaDto.builder()
            .id(guardada.getId())
            .categoria(guardada.getCategoria())
            .build();
    }
}

