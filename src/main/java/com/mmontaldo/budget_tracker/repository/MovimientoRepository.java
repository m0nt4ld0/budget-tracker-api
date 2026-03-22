package com.mmontaldo.budget_tracker.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mmontaldo.budget_tracker.entity.MovimientoEntity;
import com.mmontaldo.budget_tracker.model.TipoMovimiento;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<MovimientoEntity, Long> {

    List<MovimientoEntity> findByActivoTrue();

    Page<MovimientoEntity> findByTipoMovimientoAndFechaBetween(
        TipoMovimiento tipo, LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable);

    List<MovimientoEntity> findByTipoMovimientoAndFechaBetween(
        TipoMovimiento tipo, LocalDate fechaDesde, LocalDate fechaHasta);

    Page<MovimientoEntity> findByTipoMovimientoAndFechaBetweenAndCategoria_Id(
        TipoMovimiento tipo, LocalDate fechaDesde, LocalDate fechaHasta, Long categoriaId, Pageable pageable);

    Page<MovimientoEntity> findByCategoriaId(Long categoriaId, Pageable pageable);

    Page<MovimientoEntity> findByTipoMovimientoAndFechaBetweenAndUsuario_Id(
        TipoMovimiento tipo, LocalDate fechaDesde, LocalDate fechaHasta, Long usuarioId, Pageable pageable);
    
    List<MovimientoEntity> findByTipoMovimientoAndFechaBetweenAndUsuario_Id(
        TipoMovimiento tipo, LocalDate fechaDesde, LocalDate fechaHasta, Long usuarioId);

    Page<MovimientoEntity> findByTipoMovimientoAndFechaBetweenAndCategoria_IdAndUsuario_Id(
        TipoMovimiento tipo, LocalDate fechaDesde, LocalDate fechaHasta, Long categoriaId, Long usuarioId, Pageable pageable);

}