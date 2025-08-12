package com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso;

import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusProgresso;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "progresso_aluno")
@Entity
public class ProgressoAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrumento_id", nullable = false)
    private Instrumento instrumento;


    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private StatusProgresso status = StatusProgresso.NAO_INICIADA;

    private boolean concluido;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "percentual_conclusao")
    @Builder.Default
    private Double percentualConclusao = 0.0;


    @Column(name = "ultima_atualizacao", nullable = false)
    @Builder.Default
    private LocalDateTime ultimaAtualizacao = LocalDateTime.now();

    @Column(name = "progresso_geral", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal progressoGeral = BigDecimal.ZERO;


    @OneToMany(mappedBy = "progressoAluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProgressoDisciplina> disciplinas = new ArrayList<>();


    // Método para atualizar progresso
    public void calcularProgressoGeral() {
        if (disciplinas == null || disciplinas.isEmpty()) {
            this.progressoGeral = BigDecimal.ZERO;
            return;
        }

        BigDecimal totalDisciplinas = BigDecimal.valueOf(disciplinas.size());
        BigDecimal concluidas = BigDecimal.valueOf(disciplinas.stream()
                .filter(d -> d.getStatus() == StatusProgresso.DISCIPLINA_CONCLUIDA)
                .count());

        this.progressoGeral = concluidas.divide(totalDisciplinas, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        this.ultimaAtualizacao = LocalDateTime.now();
    }

    // Método para atualizar percentual
    public void calcularPercentualConclusao() {
        if (disciplinas == null || disciplinas.isEmpty()) {
            this.percentualConclusao = 0.0;
            return;
        }

        double totalDisciplinas = disciplinas.size();
        double concluidas = disciplinas.stream()
                .filter(d -> d.getStatus() == StatusProgresso.DISCIPLINA_CONCLUIDA)
                .count();

        this.percentualConclusao = (concluidas / totalDisciplinas) * 100;
    }

}