package com.alangodoy.studioApp.s.business.dto.in.instrumento;


import com.alangodoy.studioApp.s.business.dto.in.conteudo.DisciplinaRequestDTO;
import com.alangodoy.studioApp.s.infrastructure.enums.InstrumentoTipo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstrumentoRequestDTO {
    private Long id;
    private String nome;
    private InstrumentoTipo tipo;
    private Integer quantidadeDeAluno;
    private List<DisciplinaRequestDTO> disciplinas;
}
