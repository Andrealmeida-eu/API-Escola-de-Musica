package com.alangodoy.studioApp.s.business.dto.out;

import lombok.*;

import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HorarioDTO {
    private LocalTime horaInicio;
    private LocalTime horaFim;
}
