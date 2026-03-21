package com.mmontaldo.budget_tracker.service.impl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.config.AuditConfig;
import com.mmontaldo.budget_tracker.entity.CategoriaEntity;
import com.mmontaldo.budget_tracker.entity.MovimientoEntity;
import com.mmontaldo.budget_tracker.exception.CategoriaNotFoundException;
import com.mmontaldo.budget_tracker.exception.DatabaseConnectionException;
import com.mmontaldo.budget_tracker.exception.FechaInvalidaException;
import com.mmontaldo.budget_tracker.exception.GastoFechaFuturaException;
import com.mmontaldo.budget_tracker.exception.GastoImporteNegativoException;
import com.mmontaldo.budget_tracker.exception.RequestBodyInvalidException;
import com.mmontaldo.budget_tracker.model.TipoMovimiento;
import com.mmontaldo.budget_tracker.model.dto.CategoriaDto;
import com.mmontaldo.budget_tracker.model.dto.MovimientoDto;
import com.mmontaldo.budget_tracker.repository.CategoriaRepository;
import com.mmontaldo.budget_tracker.repository.MovimientoRepository;
import com.mmontaldo.budget_tracker.service.MovimientoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CategoriaRepository categoriaRepository;
    private final AuditConfig auditConfig;

    @Transactional
    public MovimientoDto crearMovimiento(MovimientoDto movimientoDto) {

        movimientoDto.setTipoMovimiento(TipoMovimiento.EGRESO.name());

        if (movimientoDto.getFecha().isAfter(LocalDate.now())) {
            throw new GastoFechaFuturaException("No se puede crear un movimiento con fecha futura");
        }
        if (movimientoDto.getImporte().compareTo(BigDecimal.ZERO) < 0) {
            throw new GastoImporteNegativoException("No se puede crear un movimiento con importe negativo");
        }
        if (movimientoDto.getCategoria() == null || movimientoDto.getCategoria().getId() == null) {
            throw new RequestBodyInvalidException("La categoría es obligatoria");
        }

        CategoriaEntity categoria = categoriaRepository.findById(movimientoDto.getCategoria().getId())
            .orElseThrow(() -> new CategoriaNotFoundException("Categoría no encontrada"));

        String auditUser = auditConfig.getEnabled() ? auditConfig.getDefaultUser() : null;

        MovimientoEntity entity = MovimientoEntity.builder()
            .fecha(movimientoDto.getFecha())
            .concepto(movimientoDto.getConcepto())
            .importe(movimientoDto.getImporte())
            .categoria(categoria)
            .tipoMovimiento(TipoMovimiento.EGRESO)
            .activo(true)
            .audTsIns(OffsetDateTime.now())
            .audTsInsUser(auditUser)
            .build();

        try {
            MovimientoEntity guardado = movimientoRepository.save(entity);
            return MovimientoDto.builder()
                .id(guardado.getId())
                .fecha(guardado.getFecha())
                .concepto(guardado.getConcepto())
                .importe(guardado.getImporte())
                .categoria(CategoriaDto.builder()
                    .id(categoria.getId())
                    .categoria(categoria.getCategoria())
                    .build())
                .build();
        } catch (Exception e) {
            throw new DatabaseConnectionException("Error al guardar el movimiento en la base de datos");
        }
    }

    public Page<MovimientoDto> getMovimientos(LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable, TipoMovimiento tipoMovimiento) {
        if (fechaDesde == null)
            fechaDesde = LocalDate.of(1900, 1, 1);
        if (fechaHasta == null)
            fechaHasta = LocalDate.now();
        if (fechaDesde.isAfter(fechaHasta)) {
            throw new FechaInvalidaException("La fechaDesde no puede ser mayor a la fechaHasta");
        }

        return movimientoRepository
            .findByTipoMovimientoAndFechaBetween(tipoMovimiento, fechaDesde, fechaHasta, pageable)
            .map(entity -> MovimientoDto.builder()
                .id(entity.getId())
                .fecha(entity.getFecha())
                .concepto(entity.getConcepto())
                .importe(entity.getImporte())
                .categoria(CategoriaDto.builder()
                    .id(entity.getCategoria().getId())
                    .categoria(entity.getCategoria().getCategoria())
                    .build())
                .build()
            );
    }

    public Map<String, BigDecimal> getTotalesPorCategoria(LocalDate fechaDesde, LocalDate fechaHasta, TipoMovimiento tipoMovimiento) {
        Map<String, BigDecimal> totalesPorCategoria = new HashMap<>();

        // ✅ Solo categorías del tipo correcto
        List<CategoriaEntity> categorias = categoriaRepository.findByTipoMovimiento(tipoMovimiento);

        for (CategoriaEntity categoria : categorias) {
            BigDecimal total = movimientoRepository
                .findByTipoMovimientoAndFechaBetween(tipoMovimiento, fechaDesde, fechaHasta)
                .stream()
                .filter(m -> m.getCategoria().getId().equals(categoria.getId()))
                .map(MovimientoEntity::getImporte)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            totalesPorCategoria.put(categoria.getCategoria(), total);
        }
        return totalesPorCategoria;
    }

    public Page<MovimientoDto> getMovimientosFiltradosPorPagina(
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        Long categoriaId,
        TipoMovimiento tipoMovimiento,
        Pageable pageable
    ) {
        Page<MovimientoEntity> movimientos =
            movimientoRepository.findByTipoMovimientoAndFechaBetweenAndCategoria_Id(
                tipoMovimiento, fechaDesde, fechaHasta, categoriaId, pageable
            );

        return movimientos.map(entity -> MovimientoDto.builder()
            .id(entity.getId())
            .fecha(entity.getFecha())
            .concepto(entity.getConcepto())
            .importe(entity.getImporte())
            .categoria(CategoriaDto.builder()
                .id(entity.getCategoria().getId())
                .categoria(entity.getCategoria().getCategoria())
                .build())
            .build()
        );
    }
}