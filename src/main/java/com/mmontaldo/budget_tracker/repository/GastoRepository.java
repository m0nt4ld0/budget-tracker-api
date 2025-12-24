package com.mmontaldo.budget_tracker.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mmontaldo.budget_tracker.entity.CategoriaEntity;
import com.mmontaldo.budget_tracker.entity.GastoEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GastoRepository extends JpaRepository<GastoEntity, Long> {

    List<GastoEntity> findByActivoTrue();

    Page<GastoEntity> findByFechaBetween(LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable);

    Page<GastoEntity> findByCategoriaId(Long categoriaId, Pageable pageable);

    List<GastoEntity> findByFechaBetween(LocalDate fechaDesde, LocalDate fechaHasta);
}
