package com.alangodoy.studioApp.s.business.converter.aluno;

import com.alangodoy.studioApp.s.business.dto.out.RelatorioAulaResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.RelatorioAula;
import org.springframework.stereotype.Component;

@Component
public class RelatorioAulaConverter {


    public RelatorioAulaResponseDTO toDTO(RelatorioAula entity) {
        if (entity == null) return null;

        RelatorioAulaResponseDTO dto = new RelatorioAulaResponseDTO();
        dto.setDataAula(entity.getDataAula());
        dto.setNomeProfessor(entity.getProfessor().getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setDataCriacao(entity.getDataCriacao());

            if (entity.getAluno() != null) {
                dto.setNomeAluno(entity.getAluno().getNome());
            }

        return dto;
    }
}
