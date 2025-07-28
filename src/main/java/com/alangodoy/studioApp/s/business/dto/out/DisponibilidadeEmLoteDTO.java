package com.alangodoy.studioApp.s.business.dto.out;

import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisponibilidadeEmLoteDTO {
    private List<DayOfWeek> diasSemana;
    private List<HorarioDTO> horarios;
}

