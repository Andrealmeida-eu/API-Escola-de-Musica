package com.alangodoy.studioApp.s.business.dto.out.instrumento;


import com.alangodoy.studioApp.s.business.dto.out.conteudo.DisciplinaResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.enums.InstrumentoTipo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstrumentoResponseDTO {
    private Long id;
    private String nome;
    private InstrumentoTipo tipo;
    private Integer quantidadeDeAluno;
    private List<DisciplinaResponseDTO> disciplinas;
}
