package com.mmontaldo.budget_tracker.service.impl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mmontaldo.budget_tracker.config.AuditConfig;
import com.mmontaldo.budget_tracker.entity.MonedaEntity;
import com.mmontaldo.budget_tracker.exception.MonedaNotFoundException;
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
        log.info("Creando moneda {codMoneda={}}", dto.getCodMoneda());

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

    public MonedaDto editarMoneda(Long id, MonedaDto dto) {
        log.info("Editando moneda {id={}}", id);

        Optional<MonedaEntity> entityOpt = monedaRepository.findById(id);
        if(entityOpt.isEmpty()) {
            throw new MonedaNotFoundException("Moneda no encontrada");
        }
        MonedaEntity entity = entityOpt.get();
        entity.setCodMoneda(dto.getCodMoneda());
        entity.setDescMoneda(dto.getDescMoneda());
        MonedaEntity guardada = monedaRepository.save(entity);
        return MonedaDto.builder()
                .id(guardada.getIdMoneda())
                .codMoneda(guardada.getCodMoneda())
                .descMoneda(guardada.getDescMoneda())
                .build();
    }

}