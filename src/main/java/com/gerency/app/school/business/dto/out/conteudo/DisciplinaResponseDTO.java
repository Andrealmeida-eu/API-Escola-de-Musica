package com.alangodoy.studioApp.s.business.dto.out.conteudo;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisciplinaResponseDTO {


    private Long id;
    private String nome;
    private String descricao;
    private Integer ordem;
    private Set<TopicoResponseDTO> topicos;

    // Getters e Setters

}
