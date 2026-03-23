package com.mmontaldo.budget_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

import com.mmontaldo.budget_tracker.model.TipoMovimiento;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Column(nullable = true)
    private String icono;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "aud_ts_ins", nullable = false)
    private OffsetDateTime audTsIns;

    @Column(name = "aud_ts_ins_user", nullable = false)
    private String audTsInsUser;

    @Column(name = "aud_ts_upd")
    private OffsetDateTime audTsUpd;

    @Column(name = "aud_ts_upd_user")
    private String audTsUpdUser;
}
