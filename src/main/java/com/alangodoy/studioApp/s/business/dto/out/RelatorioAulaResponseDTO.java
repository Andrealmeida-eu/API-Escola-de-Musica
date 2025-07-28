package com.alangodoy.studioApp.s.business.dto.out;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatorioAulaResponseDTO {



    private LocalDate dataAula;
    private String descricao;

    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private String nomeProfessor;
    private String nomeAluno;
    // Getters e Setters
}