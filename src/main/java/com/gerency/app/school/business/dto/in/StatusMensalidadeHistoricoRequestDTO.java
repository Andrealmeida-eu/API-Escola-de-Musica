package com.alangodoy.studioApp.s.business.dto.in;

import com.alangodoy.studioApp.s.business.dto.in.aluno.MensalidadeRequestDTO;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusMensalidade;
import lombok.*;

import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusMensalidadeHistoricoRequestDTO {


    private Long id;
    private StatusMensalidade statusAnterior;
    private StatusMensalidade statusNovo;
    private LocalDate dataModificacao;
    private MensalidadeRequestDTO mensalidade;
}
