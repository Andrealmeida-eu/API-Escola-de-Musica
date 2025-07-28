package com.alangodoy.studioApp.s.business.dto.out.receita;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DespesasResponseDTO {

    private String descricao;
    private BigDecimal valor;


    private LocalDate data;
    private String categoriaNome;
}
