package com.mmontaldo.budget_tracker.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mmontaldo.budget_tracker.entity.GastoEntity;
import com.mmontaldo.budget_tracker.model.dto.GastoDto;

import java.time.LocalDate;
import java.util.List;

public interface GastoRepository extends JpaRepository<GastoEntity, Long> {

    List<GastoEntity> findByActivoTrue();

    List<GastoEntity> findByFechaBetween(LocalDate desde, LocalDate hasta);

    List<GastoEntity> findByCategoriaId(Long categoriaId);

    Page<GastoDto> findByFechaBetween(LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable);
}
