package com.mmontaldo.budget_tracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmontaldo.budget_tracker.entity.MonedaEntity;

public interface MonedaRepository  extends JpaRepository<MonedaEntity, Long> {

    Optional<MonedaEntity> findByCodMoneda(String codMoneda);

}
