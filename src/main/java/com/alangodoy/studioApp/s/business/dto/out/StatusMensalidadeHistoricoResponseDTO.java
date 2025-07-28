package com.alangodoy.studioApp.s.business.dto.out;

import com.alangodoy.studioApp.s.business.dto.out.aluno.MensalidadeResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusMensalidade;
import lombok.*;

import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusMensalidadeHistoricoResponseDTO {



    private StatusMensalidade statusAnterior;
    private StatusMensalidade statusNovo;


    private LocalDate dataModificacao;
    private MensalidadeResponseDTO mensalidade;
}
