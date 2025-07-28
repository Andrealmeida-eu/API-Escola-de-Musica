package com.alangodoy.studioApp.s.business.dto.in.aluno;

import com.alangodoy.studioApp.s.infrastructure.enums.StatusAula;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AulaFilterDTO {

    private Long alunoId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private DayOfWeek diaSemana;
    private StatusAula status;

    // getters e setters
}
