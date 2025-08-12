package com.alangodoy.studioApp.s.business.dto.in;

import com.alangodoy.studioApp.s.infrastructure.enums.StatusReposicao;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoricoStatusReposicaoRequestDTO {

    private Long id;
    private LocalDateTime dataAlteracao;
    private StatusReposicao statusAnterior;
    private StatusReposicao novoStatus;
}
