package com.alangodoy.studioApp.s.business.dto.in.receita;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaDespesasRequestDTO {

    private Long id;
    private String nome;
    private String descricao;

}
