package com.mmontaldo.budget_tracker.service.impl;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.config.AuditConfig;
import com.mmontaldo.budget_tracker.entity.MonedaEntity;
import com.mmontaldo.budget_tracker.model.dto.MonedaDto;
import com.mmontaldo.budget_tracker.repository.MonedaRepository;
import com.mmontaldo.budget_tracker.service.MonedaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MonedaServiceImpl implements MonedaService {

    private final MonedaRepository monedaRepository;
    private final AuditConfig auditConfig;

    public List<MonedaDto> getMonedas() {
        log.info("Obteniendo monedas");
        return monedaRepository.findAll().stream()
                .map(entity -> MonedaDto.builder()
                        .id(entity.getIdMoneda())
                        .codMoneda(entity.getCodMoneda())
                        .descMoneda(entity.getDescMoneda())
                        .build())
                .toList();
    }

    public MonedaDto crearMoneda(MonedaDto dto) {
        log.info("Creando moneda: {}", dto.getCodMoneda());

        String auditUser = auditConfig.getEnabled() ? auditConfig.getDefaultUser() : "budget_tracker_api";

        MonedaEntity entity = MonedaEntity.builder()
                .codMoneda(dto.getCodMoneda())
                .descMoneda(dto.getDescMoneda())
                .audTsIns(OffsetDateTime.now())
                .audTsInsUser(auditUser)
                .build();

        MonedaEntity guardada = monedaRepository.save(entity);

        return MonedaDto.builder()
                .id(guardada.getIdMoneda())
                .codMoneda(guardada.getCodMoneda())
                .descMoneda(guardada.getDescMoneda())
                .build();
    }
}