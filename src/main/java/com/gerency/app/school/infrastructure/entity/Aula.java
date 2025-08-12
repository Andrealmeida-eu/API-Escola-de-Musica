package com.alangodoy.studioApp.s.infrastructure.entity;


import com.alangodoy.studioApp.s.infrastructure.enums.StatusAula;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusReposicao;
import com.alangodoy.studioApp.s.infrastructure.enums.TipoAula;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "aulas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aula {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;



        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "aluno_id", nullable = false)
        private Aluno aluno;

    @Column(name = "horario_aula", nullable = false)
    private LocalTime horarioAula;

    @Column(name = "horario_inicio_comercial", nullable = false)
    @Builder.Default
    private LocalTime horarioInicioComercial = LocalTime.of(8, 0);

    @Column(name = "horario_fim_comercial", nullable = false)
    @Builder.Default
    private LocalTime horarioFimComercial = LocalTime.of(22, 0);

    @Column(name = "horario_padrao")
    private LocalTime horarioPadrao;

        @Enumerated(EnumType.STRING)
        @Column(name = "dia_semana", nullable = false)
        private DayOfWeek diaSemanaAula;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TipoAula tipoAula;
 // Horário padrão para aulas recorrentes

        @Column(nullable = false)
        @Builder.Default
        private boolean recorrente = false;

        @ManyToOne
        @JoinColumn(name = "aula_base_id")
        private Aula aulaBase;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "professor_id", nullable = false)
        private Professor professor;

        @Column(name = "data_hora", nullable = false)
   //    @FutureOrPresent(message = "Data/hora deve ser atual ou futura")
        private LocalDateTime dataHora;





        @Column(nullable = false)
        @Builder.Default
        private Integer duracao = 60; // Fixo em 60 minutos

        @Column(length = 500)
        private String observacoes;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        @Builder.Default
        private StatusAula status = StatusAula.AGENDADA;

        @OneToOne(mappedBy = "aulaOriginal", cascade = CascadeType.ALL)
        private Reposicao reposicao;

        // Métodos auxiliares
        public boolean podeSerRemarcada() {
                return status == StatusAula.AGENDADA || status == StatusAula.CONFIRMADA;
        }

        public boolean foiRealizada() {
                return status == StatusAula.REALIZADA || status == StatusAula.REPOSTA;
        }

        @PreUpdate
        @PrePersist
        public void atualizarStatusReposicao() {
                if (this.reposicao != null && this.reposicao.getStatus() == StatusReposicao.REALIZADA) {
                        this.status = StatusAula.REALIZADA;
                }
        }

        public void validarHorario() {
                if (dataHora.toLocalTime().isBefore(horarioInicioComercial) ||
                        dataHora.toLocalTime().isAfter(horarioFimComercial)) {
                        throw new IllegalStateException("Aula fora do horário comercial");
                }
        }




}
