package com.alangodoy.studioApp.s.business.dto.in.receita;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DespesasRequestDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private Long categoriaId;
    private String categoriaNome;
}
