package com.alangodoy.studioApp.s.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RelatorioAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataAula;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    /*
    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turma turma;

     */

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // Getters e Setters
}
