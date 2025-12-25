package com.mmontaldo.budget_tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmontaldo.budget_tracker.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByUsuarioAndActivoTrue(String usuario);
    List<UsuarioEntity> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}
