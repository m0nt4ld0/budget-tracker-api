package com.mmontaldo.budget_tracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private CategoriaEntity categoriaPadre;

    @OneToMany(mappedBy = "categoriaPadre")
    private List<CategoriaEntity> subcategorias;

    @Column(nullable = true)
    private String icono;

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
