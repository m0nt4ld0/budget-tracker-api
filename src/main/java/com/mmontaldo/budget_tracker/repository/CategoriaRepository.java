package com.mmontaldo.budget_tracker.repository;

import com.mmontaldo.budget_tracker.entity.CategoriaEntity;
import com.mmontaldo.budget_tracker.entity.UsuarioEntity;
import com.mmontaldo.budget_tracker.model.TipoMovimiento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {

    List<CategoriaEntity> findByActivoTrue();

    List<CategoriaEntity> findByCategoriaPadreId(Long categoriaId);

    List<CategoriaEntity> findByTipoMovimiento(TipoMovimiento tipoMovimiento);
    
    List<CategoriaEntity> findByUsuarioAndActivoTrue(UsuarioEntity usuario);
}

