package com.alangodoy.studioApp.s.business.converter.aluno;

import com.alangodoy.studioApp.s.business.dto.in.ReposicaoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.ReposicaoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.AulaResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.Aula;
import com.alangodoy.studioApp.s.infrastructure.entity.Reposicao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AulaConverter {


    public Aula toEntity(AulaResponseDTO dto) {
        if (dto == null) return null;

        return Aula.builder()
                .diaSemanaAula(dto.getDiaSemanaAula())
                .horarioAula(dto.getHorarioAula())
                .dataHora(dto.getDataHora())
                .duracao(dto.getDuracao())
                .observacoes(dto.getObservacoes())
                .status(dto.getStatus())
                .build();
    }

    public AulaResponseDTO toDTO(Aula entity) {
        if (entity == null) return null;
        AulaResponseDTO dto = new AulaResponseDTO();
        dto.setId(entity.getId());
        dto.setDiaSemanaAula(entity.getDiaSemanaAula());
        dto.setTipoAula(entity.getTipoAula());
        dto.setAlunoNome(entity.getAluno().getNome());
        dto.setProfessorNome(entity.getProfessor().getNome());
        dto.setHorarioAula(entity.getHorarioAula());
        dto.setDataHora(entity.getDataHora());
        dto.setInstrumentoNome(entity.getAluno().getInstrumento().getNome());
        dto.setDuracao(entity.getDuracao());
        dto.setObservacoes(entity.getObservacoes());
        dto.setStatus(entity.getStatus());


        return dto;
    }

    public List<AulaResponseDTO> toDTOList(List<Aula> aulas) {
        return aulas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReposicaoResponseDTO> toReposicaoDTOList(List<Reposicao> reposicoes) {
        return reposicoes.stream()
                .map(this::toReposicaoDTO)
                .collect(Collectors.toList());
    }

    public ReposicaoResponseDTO toReposicaoDTO(Reposicao reposicao) {
        ReposicaoResponseDTO dto = new ReposicaoResponseDTO();
        dto.setNovaDataHora(reposicao.getAulaReposicao().getDataHora());
        dto.setMotivo(reposicao.getMotivo());
        dto.setStatus(reposicao.getStatus());
        dto.setTipoAula(reposicao.getTipoAula());
        dto.setAlunoNome(reposicao.getAulaOriginal().getAluno().getNome());
        dto.setDataHoraAulaOriginal(reposicao.getAulaOriginal().getDataHora());
        dto.setDataSolicitacao(reposicao.getDataSolicitacao());
        dto.setId(reposicao.getId());
        // Adicione outros campos conforme necessário
        return dto;
    }
}

