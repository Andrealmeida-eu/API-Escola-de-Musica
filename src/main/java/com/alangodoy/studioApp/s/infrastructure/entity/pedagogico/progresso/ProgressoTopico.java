package com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso;

import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Topico;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusTopico;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "progresso_topico")
@Entity
public class ProgressoTopico {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progresso_disciplina_id", nullable = false)
    private ProgressoDisciplina progressoDisciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private StatusTopico status = StatusTopico.TOPICO_NAO_INICIADO;

    private boolean concluido;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;


    @Column(name = "ultima_atualizacao", nullable = false)
    @Builder.Default
    private LocalDateTime ultimaAtualizacao = LocalDateTime.now();

    @Column(name = "progresso", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal progresso = BigDecimal.ZERO;


    @Column(length = 1000)
    private String observacoes;

    // Getters e Setters


    public void concluir() {
        this.status = StatusTopico.TOPICO_CONCLUIDO;
        this.concluido = true;
        this.dataConclusao = LocalDate.now();
        this.progresso = BigDecimal.valueOf(100);
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    public void marcarComoEmAndamento() {
        this.status = StatusTopico.TOPICO_EM_ANDAMENTO;
        this.concluido = false;
        this.dataInicio = LocalDate.now();
        this.progresso = BigDecimal.valueOf(50);
        this.ultimaAtualizacao = LocalDateTime.now();
    }


}

