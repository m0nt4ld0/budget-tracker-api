package com.mmontaldo.budget_tracker.repository;

import java.util.List;
import java.util.Optional;

import com.mmontaldo.budget_tracker.entity.UsuarioEntity;

public interface UsuarioRepository {
    Optional<UsuarioEntity> findByUsuarioAndActivoTrue(String usuario);
    List<UsuarioEntity> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}
