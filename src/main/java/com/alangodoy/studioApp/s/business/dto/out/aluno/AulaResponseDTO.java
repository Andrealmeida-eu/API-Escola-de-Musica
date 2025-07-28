package com.alangodoy.studioApp.s.business.dto.out.aluno;


import com.alangodoy.studioApp.s.business.dto.in.ReposicaoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.ReposicaoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Reposicao;
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
public class AulaResponseDTO {

private Long id;
    @Future(message = "Data deve ser no futuro")
    @NotNull(message = "Data/hora é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataHora;


    private String alunoNome;



    private TipoAula tipoAula;

    private String professorNome;

    private String InstrumentoNome;

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

}
