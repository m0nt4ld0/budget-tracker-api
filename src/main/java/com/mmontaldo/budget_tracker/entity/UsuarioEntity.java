package com.mmontaldo.budget_tracker.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.*;

@Entity
@Table(
    name = "usuarios",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_usuarios_usuario", columnNames = "usuario")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String usuario;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "aud_ts_ins", nullable = false, updatable = false)
    private OffsetDateTime audTsIns;

    @Column(name = "aud_ts_ins_user", nullable = false, length = 255, updatable = false)
    private String audTsInsUser;

    @Column(name = "aud_ts_upd")
    private OffsetDateTime audTsUpd;

    @Column(name = "aud_ts_upd_user", length = 255)
    private String audTsUpdUser;

    @PrePersist
    protected void onCreate() {
        this.audTsIns = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.audTsUpd = OffsetDateTime.now();
    }
}
