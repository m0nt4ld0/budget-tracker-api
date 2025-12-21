package com.mmontaldo.budget_tracker.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "gastos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaEntity categoria;

    @Column(nullable = false, length = 255)
    private String concepto;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal importe;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "aud_ts_ins", nullable = false)
    private OffsetDateTime audTsIns;

    @Column(name = "aud_ts_ins_user", nullable = false)
    private String audTsInsUser;

    @Column(name = "aud_ts_upd")
    private OffsetDateTime audTsUpd;

    @Column(name = "aud_ts_upd_user")
    private String audTsUpdUser;
}
