package com.mmontaldo.budget_tracker.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.config.AuditConfig;
import com.mmontaldo.budget_tracker.entity.CategoriaEntity;
import com.mmontaldo.budget_tracker.entity.GastoEntity;
import com.mmontaldo.budget_tracker.exception.CategoriaNotFoundException;
import com.mmontaldo.budget_tracker.exception.DatabaseConnectionException;
import com.mmontaldo.budget_tracker.exception.FechaInvalidaException;
import com.mmontaldo.budget_tracker.exception.GastoFechaFuturaException;
import com.mmontaldo.budget_tracker.exception.GastoImporteNegativoException;
import com.mmontaldo.budget_tracker.exception.RequestBodyInvalidException;
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

        public GastoDto crearGasto(GastoDto gastoDto) {
                
                if (gastoDto.getFecha().isAfter(LocalDate.now())) {
                        throw new GastoFechaFuturaException("No se puede crear un gasto con fecha futura");
                }
                if (gastoDto.getImporte() < 0) {
                        throw new GastoImporteNegativoException("No se puede crear un gasto con importe negativo");
                }
                if (gastoDto.getCategoria() == null || gastoDto.getCategoria().getId() == null) {
                        throw new RequestBodyInvalidException("La categoría es obligatoria");
                }

                CategoriaEntity categoria = categoriaRepository.findById(gastoDto.getCategoria().getId())
                                .orElseThrow(() -> new CategoriaNotFoundException("Categoría no encontrada"));

                String auditUser = auditConfig.getEnabled() ? auditConfig.getDefaultUser() : null;

                GastoEntity entity = GastoEntity.builder()
                                .fecha(gastoDto.getFecha())
                                .concepto(gastoDto.getConcepto())
                                .importe(BigDecimal.valueOf(gastoDto.getImporte()))
                                .categoria(categoria)
                                .activo(true)
                                .audTsIns(OffsetDateTime.now())
                                .audTsInsUser(auditUser)
                                .build();

                try {
                        GastoEntity guardado = gastoRepository.save(entity);
                        return GastoDto.builder()
                                        .id(guardado.getId())
                                        .fecha(guardado.getFecha())
                                        .concepto(guardado.getConcepto())
                                        .importe(guardado.getImporte().doubleValue())
                                        .categoria(CategoriaDto.builder()
                                                        .id(categoria.getId())
                                                        .categoria(categoria.getCategoria())
                                                        .build())
                                        .build();
                } catch (Exception e) {
                        throw new DatabaseConnectionException("Error al guardar el gasto en la base de datos");
                }
        }

        public Page<GastoDto> getGastos(LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable) {
                if (fechaDesde == null)
                        fechaDesde = LocalDate.of(1900, 1, 1);
                if (fechaHasta == null)
                        fechaHasta = LocalDate.now();
                if (fechaDesde.isAfter(fechaHasta)) {
                        throw new FechaInvalidaException("La fechaDesde no puede ser mayor a la fechaHasta");
                }

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

        public Map<String, Double> getTotalesPorCategoria(LocalDate fechaDesde, LocalDate fechaHasta) {
            Map<String, Double> totalesPorCategoria = new HashMap<>();

            for (CategoriaEntity categoria : categoriaRepository.findAll()) {
                Double total = gastoRepository.findByFechaBetween(fechaDesde, fechaHasta)
                        .stream()
                        .filter(gasto -> gasto.getCategoria().getId().equals(categoria.getId()))
                        .mapToDouble(gasto -> gasto.getImporte().doubleValue())
                        .sum();
                totalesPorCategoria.put(categoria.getCategoria(), total);
            }
            return totalesPorCategoria;
        }

}
