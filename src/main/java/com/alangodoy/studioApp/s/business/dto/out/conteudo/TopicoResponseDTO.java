package com.alangodoy.studioApp.s.business.dto.out.conteudo;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicoResponseDTO {


    private Long id;
    private String nome;

    @Builder.Default
    private int ordem = 1;


    // Getters e Setters
}

