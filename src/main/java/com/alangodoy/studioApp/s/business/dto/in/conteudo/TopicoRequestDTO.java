package com.alangodoy.studioApp.s.business.dto.in.conteudo;

import jakarta.validation.constraints.Min;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicoRequestDTO {

    private Long id;

    private Long disciplinaId;

    private String nome;


    @Min(value = 1, message = "Ordem deve ser no mínimo 1")
    @Builder.Default
    private int ordem = 1;


    // Getters e Setters
}

