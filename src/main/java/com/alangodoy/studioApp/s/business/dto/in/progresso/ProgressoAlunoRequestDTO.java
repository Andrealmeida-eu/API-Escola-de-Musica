package com.alangodoy.studioApp.s.business.dto.in.progresso;

import com.alangodoy.studioApp.s.business.dto.in.instrumento.InstrumentoRequestDTO;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusProgresso;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressoAlunoRequestDTO {

    private Long id;
    private Long alunoId;
    private String alunoNome;
    private Long instrumentoId;
    private String instrumentoNome;
    private InstrumentoRequestDTO instrumento;
    private StatusProgresso status;
    private LocalDate dataInicio;

    private LocalDate ultimaAtualizacao;
    private Double percentualConclusao;
    private List<ProgressoDisciplinaRequestDTO> disciplinas;
}
