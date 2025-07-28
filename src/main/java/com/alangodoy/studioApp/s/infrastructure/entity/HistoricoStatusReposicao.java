package com.alangodoy.studioApp.s.infrastructure.entity;

import com.alangodoy.studioApp.s.infrastructure.enums.StatusReposicao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "historico_status_reposicao")
public class HistoricoStatusReposicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reposicao_id")
    private Reposicao reposicao;

    @Column(nullable = false)
    private LocalDateTime dataAlteracao;

    @Enumerated(EnumType.STRING)
    private StatusReposicao statusAnterior;

    @Enumerated(EnumType.STRING)
    private StatusReposicao novoStatus;
}
