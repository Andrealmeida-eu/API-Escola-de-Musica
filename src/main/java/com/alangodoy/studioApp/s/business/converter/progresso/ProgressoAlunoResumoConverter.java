package com.alangodoy.studioApp.s.business.converter.progresso;

import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoAlunoResumoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoAluno;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusProgresso;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProgressoAlunoResumoConverter {

    public ProgressoAlunoResumoResponseDTO toProgressoAlunoResumoDTO(ProgressoAluno entity) {
        if (entity == null) {
            return null;
        }

        ProgressoAlunoResumoResponseDTO dto = new ProgressoAlunoResumoResponseDTO();
        dto.setId(entity.getId());

        // Mapeamento do Instrumento
        if (entity.getInstrumento() != null) {
            dto.setInstrumentoId(entity.getInstrumento().getId());
            dto.setInstrumentoNome(entity.getInstrumento().getNome());
        }

        // Mapeamento dos campos de progresso
        dto.setStatus(entity.getStatus());
        dto.setProgressoGeral(entity.getProgressoGeral());
        dto.setUltimaAtualizacao(entity.getUltimaAtualizacao());
        dto.setDataInicio(entity.getDataInicio());

        // Calcula resumo das disciplinas
        if (entity.getDisciplinas() != null) {
            dto.setTotalDisciplinas(entity.getDisciplinas().size());
            dto.setDisciplinasConcluidas((int) entity.getDisciplinas().stream()
                    .filter(d -> d.getStatus() == StatusProgresso.DISCIPLINA_CONCLUIDA)
                    .count());
        }

        return dto;
    }

    public List<ProgressoAlunoResumoResponseDTO> toDTOList(List<ProgressoAluno> entities) {
        return entities.stream()
                .map(this::toProgressoAlunoResumoDTO)
                .collect(Collectors.toList());
    }
}
