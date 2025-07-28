package com.alangodoy.studioApp.s.business.dto.in.receita;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceitaRequestDTO {

    private double receitaTotal;
    private double custoTotal;
    private double lucroTotal;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int TotalMensalidades;
    private int mensalidadesPagas;
    private int mensalidadesPendentes;
}
