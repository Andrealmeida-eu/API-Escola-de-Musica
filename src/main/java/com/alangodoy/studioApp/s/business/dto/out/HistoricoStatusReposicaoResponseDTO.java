package com.alangodoy.studioApp.s.business.dto.out;

import com.alangodoy.studioApp.s.infrastructure.enums.StatusReposicao;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoricoStatusReposicaoResponseDTO {

    private Long id;


    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataAlteracao;
    private StatusReposicao statusAnterior;
    private StatusReposicao novoStatus;
}
