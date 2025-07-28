package com.alangodoy.studioApp.s.business.converter.conteudo;

import com.alangodoy.studioApp.s.business.dto.in.conteudo.TopicoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.TopicoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Topico;
import org.springframework.stereotype.Component;

@Component
public class TopicoConverter {

    public TopicoResponseDTO toTopicoDTO(Topico topico) {
        TopicoResponseDTO dto = new TopicoResponseDTO();
        dto.setId(topico.getId());
        dto.setNome(topico.getNome());
        dto.setOrdem(topico.getOrdem());
        return dto;
    }

    public Topico toTopicoEntity(TopicoRequestDTO dto) {
        Topico topico = new Topico();
        topico.setId(dto.getId());
        topico.setNome(dto.getNome());
        topico.setOrdem(dto.getOrdem());
        return topico;
    }
}
