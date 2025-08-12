package com.alangodoy.studioApp.s.business.dto.in.progresso;

import com.alangodoy.studioApp.s.infrastructure.enums.StatusProgresso;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressoAlunoResumoRequestDTO {
    private Long id;
    private Long instrumentoId;
    private String instrumentoNome;
    private StatusProgresso status;
    private LocalDate dataInicio;
    private BigDecimal progressoGeral;
    private LocalDateTime ultimaAtualizacao;
    private Integer totalDisciplinas;
    private Integer disciplinasConcluidas;
}
