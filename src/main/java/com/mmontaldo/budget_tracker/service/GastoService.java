package com.mmontaldo.budget_tracker.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.config.AuditConfig;
import com.mmontaldo.budget_tracker.entity.CategoriaEntity;
import com.mmontaldo.budget_tracker.entity.GastoEntity;
import com.mmontaldo.budget_tracker.model.dto.CategoriaDto;
import com.mmontaldo.budget_tracker.model.dto.GastoDto;
import com.mmontaldo.budget_tracker.repository.CategoriaRepository;
import com.mmontaldo.budget_tracker.repository.GastoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepository;
    private final CategoriaRepository categoriaRepository;
    private final AuditConfig auditConfig;

    public Page<GastoDto> getGastos(LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable) {
        if (fechaDesde == null) fechaDesde = LocalDate.of(1900, 1, 1);
        if (fechaHasta == null) fechaHasta = LocalDate.now();

        return gastoRepository.findByFechaBetween(fechaDesde, fechaHasta, pageable)
                .map(entity -> GastoDto.builder()
                        .id(entity.getId())
                        .fecha(entity.getFecha())
                        .concepto(entity.getConcepto())
                        .importe(entity.getImporte().doubleValue())
                        .categoria(CategoriaDto.builder()
                                .id(entity.getCategoria().getId())
                                .categoria(entity.getCategoria().getCategoria())
                                .build())
                        .build()
                );
    }

    public GastoDto crearGasto(GastoDto gastoDto) {
        CategoriaEntity categoria = categoriaRepository.findById(gastoDto.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        String auditUser = auditConfig.getEnabled()
                ? auditConfig.getDefaultUser()
                : null;

        GastoEntity entity = GastoEntity.builder()
                .fecha(gastoDto.getFecha())
                .concepto(gastoDto.getConcepto())
                .importe(BigDecimal.valueOf(gastoDto.getImporte()))
                .categoria(categoria)
                .activo(true)
                .audTsIns(OffsetDateTime.now())
                .audTsInsUser(auditUser)
                .build();

        GastoEntity guardado = gastoRepository.save(entity);

        return GastoDto.builder()
            .id(guardado.getId())
            .fecha(guardado.getFecha())
            .concepto(guardado.getConcepto())
            .importe(guardado.getImporte().doubleValue())
            .categoria(CategoriaDto.builder()
                    .id(guardado.getCategoria().getId())
                    .categoria(guardado.getCategoria().getCategoria())
                    .build())
            .build();
    }

}

