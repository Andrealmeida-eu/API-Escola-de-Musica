package com.alangodoy.studioApp.s.business.converter.aluno;

import com.alangodoy.studioApp.s.business.dto.out.HistoricoStatusReposicaoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.HistoricoStatusReposicao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HistoricoStatusReposicaoConverter {

    public HistoricoStatusReposicaoResponseDTO toDTO(HistoricoStatusReposicao historico) {
        return HistoricoStatusReposicaoResponseDTO.builder()
                .id(historico.getId())
                .dataAlteracao(historico.getDataAlteracao())
                .statusAnterior(historico.getStatusAnterior())
                .novoStatus(historico.getNovoStatus())
                .build();
    }

    public List<HistoricoStatusReposicaoResponseDTO> toDTOList(List<HistoricoStatusReposicao> historicos) {
        return historicos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public HistoricoStatusReposicao toEntity(HistoricoStatusReposicaoResponseDTO historicoDTO) {
        return HistoricoStatusReposicao.builder()
                .dataAlteracao(historicoDTO.getDataAlteracao())
                .statusAnterior(historicoDTO.getStatusAnterior())
                .novoStatus(historicoDTO.getNovoStatus())
                .build();
    }
}
