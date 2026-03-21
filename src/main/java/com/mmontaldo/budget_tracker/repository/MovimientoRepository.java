package com.mmontaldo.budget_tracker.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mmontaldo.budget_tracker.entity.MovimientoEntity;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<MovimientoEntity, Long> {

    List<MovimientoEntity> findByActivoTrue();

    Page<MovimientoEntity> findByFechaBetween(LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable);

    Page<MovimientoEntity> findByCategoriaId(Long categoriaId, Pageable pageable);

    List<MovimientoEntity> findByFechaBetween(LocalDate fechaDesde, LocalDate fechaHasta);

    Page<MovimientoEntity> findByFechaBetweenAndCategoria_Categoria(
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        Long categoriaId,
        Pageable pageable
    );


}
