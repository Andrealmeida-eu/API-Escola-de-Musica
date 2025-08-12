package com.alangodoy.studioApp.s.business.dto.in.aluno;


import com.alangodoy.studioApp.s.infrastructure.enums.StatusAula;
import com.alangodoy.studioApp.s.infrastructure.enums.TipoAula;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AulaRequestDTO {

    private Long id;

    @NotNull(message = "Aluno é obrigatório")
    private Long alunoId;

    @NotNull(message = "Professor é obrigatório")
    private Long professorId;


    @Future(message = "Data deve ser no futuro")
    @NotNull(message = "Data/hora é obrigatória")
    private LocalDateTime dataHora;

    private TipoAula tipoAula;

    @NotNull(message = "Dia da semana da aula é obrigatório")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private DayOfWeek diaSemanaAula;

    @NotNull(message = "Horário da aula é obrigatório")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioAula;

    @Column(nullable = false)
    @Builder.Default
    private Integer duracao = 60; // Fixo em 60 minutos

    private String observacoes;
@Builder.Default
    private StatusAula status = StatusAula.AGENDADA;
  /*  @Future(message = "Data deve ser no futuro")
    @NotNull(message = "Data/hora é obrigatória")
    private String observacoes;
*/


    // Getters e Setters
}
