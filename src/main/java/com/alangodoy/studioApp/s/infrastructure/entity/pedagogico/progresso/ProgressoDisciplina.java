package com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso;


import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Disciplina;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusProgresso;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusTopico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "progresso_disciplina")
public class ProgressoDisciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progresso_aluno_id", nullable = true)
    private ProgressoAluno progressoAluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusProgresso status = StatusProgresso.NAO_INICIADA;

    private boolean concluido;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;

    @Column(name = "ultima_atualizacao", nullable = false)
    private LocalDateTime ultimaAtualizacao = LocalDateTime.now();

    @Column(name = "progresso", precision = 5, scale = 2)
    private BigDecimal progresso = BigDecimal.ZERO;


    @Column(length = 1000)
    private String observacoes;

    @OneToMany(mappedBy = "progressoDisciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgressoTopico> topicos = new ArrayList<>();

    public void verificarConclusaoAutomatica() {
        if (topicos == null || topicos.isEmpty()) {
            this.status = StatusProgresso.NAO_INICIADA;
            this.concluido = false;
            return;
        }

        boolean todosConcluidos = topicos.stream()
                .allMatch(t -> t.getStatus() == StatusTopico.TOPICO_CONCLUIDO);

        if (todosConcluidos) {
            this.status = StatusProgresso.DISCIPLINA_CONCLUIDA;
            this.concluido = true;
            this.dataConclusao = LocalDate.now();
        } else {
            this.status = StatusProgresso.EM_ANDAMENTO;
            this.concluido = false;
            this.dataConclusao = null;
        }

        calcularProgresso();
    }

    // Método para atualizar progresso
    public void calcularProgresso() {
        if (topicos == null || topicos.isEmpty()) {
            this.progresso = BigDecimal.ZERO;
            return;
        }

        BigDecimal totalTopicos = BigDecimal.valueOf(topicos.size());
        BigDecimal concluidos = BigDecimal.valueOf(topicos.stream()
                .filter(t -> t.getStatus() == StatusTopico.TOPICO_CONCLUIDO)
                .count());

        this.progresso = concluidos.divide(totalTopicos, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        this.ultimaAtualizacao = LocalDateTime.now();

        // Propaga atualização para a progressão do aluno
        if (this.progressoAluno != null) {
            this.progressoAluno.calcularProgressoGeral();
        }
    }


    // Getters e Setters

}


