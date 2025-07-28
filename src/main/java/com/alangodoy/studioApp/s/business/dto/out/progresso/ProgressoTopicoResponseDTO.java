package com.alangodoy.studioApp.s.business.dto.out.progresso;

import com.alangodoy.studioApp.s.infrastructure.enums.StatusTopico;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressoTopicoResponseDTO {

    private Long id;
    private String topicoNome;
    private StatusTopico status;
    private Long topicoId;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataConclusao;
    private boolean concluido;
    private int ordem;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Builder.Default
    private LocalDateTime ultimaAtualizacao = LocalDateTime.now();
    @Builder.Default
    private BigDecimal progresso = BigDecimal.ZERO;
}
