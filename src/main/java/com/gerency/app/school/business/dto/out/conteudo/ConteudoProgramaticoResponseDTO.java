package com.alangodoy.studioApp.s.business.dto.out.conteudo;


import com.alangodoy.studioApp.s.business.dto.in.instrumento.InstrumentoRequestDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ConteudoProgramaticoResponseDTO {

    private InstrumentoRequestDTO instrumento;
    private Long id;
    private Long instrumentoId;
    private String instrumentoNome;
    private List<DisciplinaResponseDTO> disciplinas;


    // Getters e Setters
}

