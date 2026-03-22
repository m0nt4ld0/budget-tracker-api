package com.mmontaldo.budget_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "monedas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonedaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMoneda;

    @Column(nullable = false, length = 3)
    private String codMoneda;

    @Column(nullable = false, length = 255)
    private String descMoneda;
    
    @Column(name = "aud_ts_ins", nullable = false)
    private OffsetDateTime audTsIns;

    @Column(name = "aud_ts_ins_user", nullable = false)
    private String audTsInsUser;

}
