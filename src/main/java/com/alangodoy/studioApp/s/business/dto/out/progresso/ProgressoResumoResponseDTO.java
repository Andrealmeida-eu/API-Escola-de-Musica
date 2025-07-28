package com.alangodoy.studioApp.s.business.dto.out.progresso;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressoResumoResponseDTO {

    private Long alunoId;
    private String alunoNome;
    private long totalDisciplinas;
    private long disciplinasConcluidas;
    private long totalTopicos;
    private long topicosConcluidos;
    private BigDecimal progressoGeral;
}
