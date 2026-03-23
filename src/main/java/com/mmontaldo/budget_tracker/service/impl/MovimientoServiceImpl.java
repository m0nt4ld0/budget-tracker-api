package com.mmontaldo.budget_tracker.service.impl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.config.AuditConfig;
import com.mmontaldo.budget_tracker.entity.CategoriaEntity;
import com.mmontaldo.budget_tracker.entity.MovimientoEntity;
import com.mmontaldo.budget_tracker.entity.MonedaEntity;
import com.mmontaldo.budget_tracker.entity.UsuarioEntity;
import com.mmontaldo.budget_tracker.exception.CategoriaNotFoundException;
import com.mmontaldo.budget_tracker.exception.FechaInvalidaException;
import com.mmontaldo.budget_tracker.exception.GastoFechaFuturaException;
import com.mmontaldo.budget_tracker.exception.GastoImporteNegativoException;
import com.mmontaldo.budget_tracker.exception.MonedaNotFoundException;
import com.mmontaldo.budget_tracker.exception.RequestBodyInvalidException;
import com.mmontaldo.budget_tracker.exception.UsuarioNoAutorizadoException;
import com.mmontaldo.budget_tracker.model.TipoMovimiento;
import com.mmontaldo.budget_tracker.model.dto.CategoriaDto;
import com.mmontaldo.budget_tracker.model.dto.MonedaDto;
import com.mmontaldo.budget_tracker.model.dto.MovimientoDto;
import com.mmontaldo.budget_tracker.repository.CategoriaRepository;
import com.mmontaldo.budget_tracker.repository.MovimientoRepository;
import com.mmontaldo.budget_tracker.repository.MonedaRepository;
import com.mmontaldo.budget_tracker.repository.UsuarioRepository;
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
    private final MonedaRepository monedaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditConfig auditConfig;

    @Transactional
    public MovimientoDto crearMovimiento(MovimientoDto movimientoDto) {

        if (movimientoDto.getMoneda() == null || movimientoDto.getMoneda().getCodMoneda() == null) {
            throw new RequestBodyInvalidException("La moneda es obligatoria");
        }

        if (movimientoDto.getFecha().isAfter(LocalDate.now())) {
            throw new GastoFechaFuturaException("No se puede crear un movimiento con fecha futura");
        }

        if (movimientoDto.getImporte().compareTo(BigDecimal.ZERO) < 0) {
            throw new GastoImporteNegativoException("No se puede crear un movimiento con importe negativo");
        }

        if (movimientoDto.getCategoria() == null || movimientoDto.getCategoria().getId() == null) {
            throw new RequestBodyInvalidException("La categoría es obligatoria");
        }

        UsuarioEntity usuario = getUsuarioAutenticado();

        CategoriaEntity categoria = categoriaRepository.findById(movimientoDto.getCategoria().getId())
            .orElseThrow(() -> new CategoriaNotFoundException("Categoría no encontrada"));

        MonedaEntity moneda = monedaRepository.findByCodMoneda(movimientoDto.getMoneda().getCodMoneda())
            .orElseThrow(() -> new MonedaNotFoundException("Moneda no encontrada"));

        String auditUser = auditConfig.getEnabled() ? auditConfig.getDefaultUser() : null;

        MovimientoEntity entity = MovimientoEntity.builder()
            .fecha(movimientoDto.getFecha())
            .concepto(movimientoDto.getConcepto())
            .importe(movimientoDto.getImporte())
            .categoria(categoria)
            .moneda(moneda)
            .usuario(usuario)
            .tipoMovimiento(TipoMovimiento.valueOf(movimientoDto.getTipoMovimiento()))
            .activo(true)
            .audTsIns(OffsetDateTime.now())
            .audTsInsUser(auditUser)
            .build();

        return toDto(movimientoRepository.save(entity));
    }

    public Page<MovimientoDto> getMovimientos(
    LocalDate fechaDesde,
    LocalDate fechaHasta,
    TipoMovimiento tipoMovimiento,
    Pageable pageable)
    {
        UsuarioEntity usuario = getUsuarioAutenticado();

        if (fechaDesde == null) fechaDesde = LocalDate.of(1900, 1, 1);
        if (fechaHasta == null) fechaHasta = LocalDate.now();

        if (fechaDesde.isAfter(fechaHasta)) {
            throw new FechaInvalidaException("La fechaDesde no puede ser mayor a la fechaHasta");
        }

        return movimientoRepository
            .findByTipoMovimientoAndFechaBetweenAndUsuario_Id(
                tipoMovimiento,
                fechaDesde,
                fechaHasta,
                usuario.getId(),
                pageable
            )
            .map(this::toDto);
    }

    public Map<String, BigDecimal> getTotalesPorCategoria(
    LocalDate fechaDesde,
    LocalDate fechaHasta,
    TipoMovimiento tipoMovimiento)
    {
        UsuarioEntity usuario = getUsuarioAutenticado();

        Map<String, BigDecimal> totales = new HashMap<>();

        List<CategoriaEntity> categorias = categoriaRepository.findByTipoMovimiento(tipoMovimiento);

        List<MovimientoEntity> movimientos = movimientoRepository
            .findByTipoMovimientoAndFechaBetweenAndUsuario_Id(
                tipoMovimiento, fechaDesde, fechaHasta, usuario.getId());

        for (CategoriaEntity categoria : categorias) {
            BigDecimal total = movimientos.stream()
                .filter(m -> m.getCategoria().getId().equals(categoria.getId()))
                .map(MovimientoEntity::getImporte)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            totales.put(categoria.getCategoria(), total);
        }

        return totales;
    }

    public Page<MovimientoDto> getMovimientosFiltradosPorPagina(
    LocalDate fechaDesde,
    LocalDate fechaHasta,
    Long categoriaId,
    TipoMovimiento tipoMovimiento,
    Pageable pageable)
    {
        UsuarioEntity usuario = getUsuarioAutenticado();

        return movimientoRepository
            .findByTipoMovimientoAndFechaBetweenAndCategoria_IdAndUsuario_Id(
                tipoMovimiento,
                fechaDesde,
                fechaHasta,
                categoriaId,
                usuario.getId(),
                pageable
            )
            .map(this::toDto);
    }

    private MovimientoDto toDto(MovimientoEntity entity) {
        return MovimientoDto.builder()
            .id(entity.getId())
            .fecha(entity.getFecha())
            .concepto(entity.getConcepto())
            .importe(entity.getImporte())
            .moneda(MonedaDto.builder()
                .id(entity.getMoneda().getIdMoneda())
                .codMoneda(entity.getMoneda().getCodMoneda())
                .descMoneda(entity.getMoneda().getDescMoneda())
                .build())
            .categoria(CategoriaDto.builder()
                .id(entity.getCategoria().getId())
                .categoria(entity.getCategoria().getCategoria())
                .build())
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