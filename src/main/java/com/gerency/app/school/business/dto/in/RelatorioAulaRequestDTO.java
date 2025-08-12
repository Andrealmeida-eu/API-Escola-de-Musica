package com.alangodoy.studioApp.s.business.dto.in;

import com.alangodoy.studioApp.s.business.dto.in.aluno.ProfessorRequestDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelatorioAulaRequestDTO {

    private Long id;
    private LocalDate dataAula;
    private Long professorId;
    private Long alunoId;
    private String descricao;
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private ProfessorRequestDTO professor;
    private String nomeProfessor;
    private String nomeAluno;
    // Getters e Setters
}