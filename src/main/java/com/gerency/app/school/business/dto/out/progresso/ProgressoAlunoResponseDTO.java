package com.alangodoy.studioApp.s.business.dto.out.progresso;

import com.alangodoy.studioApp.s.business.dto.out.instrumento.InstrumentoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusProgresso;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressoAlunoResponseDTO {

    private Long id;
    private Long alunoId;
    private String alunoNome;
    private Long instrumentoId;
    private String instrumentoNome;
    private StatusProgresso status;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate ultimaAtualizacao;
    private Double percentualConclusao;
    private List<ProgressoDisciplinaResponseDTO> disciplinas;
}
