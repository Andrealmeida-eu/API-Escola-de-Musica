package com.alangodoy.studioApp.s.business.dto.in.conteudo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisciplinaRequestDTO {


    private Long id;

    @NotBlank(message = "Nome da disciplina é obrigatório")
    private String nome;

    private String descricao;
    @NotNull
    private Integer ordem;

    private Long conteudoId;

    @Valid
    private List<TopicoRequestDTO> topicos;

    // Getters e Setters

}
