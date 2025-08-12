package com.alangodoy.studioApp.s.business.converter.aluno;

import com.alangodoy.studioApp.s.business.dto.out.ReposicaoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Reposicao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReposicaoConverter {


    public Reposicao toEntity(ReposicaoResponseDTO dto) {
        if (dto == null) return null;

        return Reposicao.builder()
                .novaDataHora(dto.getNovaDataHora())
                .motivo(dto.getMotivo())
                .status(dto.getStatus())
                .dataSolicitacao(dto.getDataSolicitacao())
                .build();
    }

    public ReposicaoResponseDTO toDTO(Reposicao entity) {
        if (entity == null) return null;

        ReposicaoResponseDTO dto = new ReposicaoResponseDTO();
        dto.setId(entity.getId());
        dto.setNovaDataHora(entity.getNovaDataHora());
        dto.setMotivo(entity.getMotivo());
        dto.setStatus(entity.getStatus());
        dto.setDataSolicitacao(entity.getDataSolicitacao());
        dto.setDataHoraAulaOriginal(entity.getAulaOriginal().getDataHora());
        dto.setAlunoNome(entity.getAulaOriginal().getAluno().getNome());
        dto.setTipoAula(entity.getTipoAula());




        if (entity.getAulaReposicao() != null) {
            dto.setAulaReposicao(entity.getAulaReposicao());
        }

        return dto;
    }

    public List<ReposicaoResponseDTO> toDTOList(List<Reposicao> historicos) {
        return historicos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
