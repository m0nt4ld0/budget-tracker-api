package com.mmontaldo.budget_tracker.repository;

import com.mmontaldo.budget_tracker.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {

    List<CategoriaEntity> findByActivoTrue();

    List<CategoriaEntity> findByCategoriaPadreId(Long categoriaId);
}

