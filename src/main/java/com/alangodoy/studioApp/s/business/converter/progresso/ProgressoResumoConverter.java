package com.alangodoy.studioApp.s.business.converter.progresso;

import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoResumoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoAluno;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusProgresso;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusTopico;
import com.alangodoy.studioApp.s.infrastructure.repository.AlunoRepository;
import com.alangodoy.studioApp.s.infrastructure.repository.ProgressoAlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

    @Component
    @RequiredArgsConstructor
    public class ProgressoResumoConverter {

        private final AlunoRepository alunoRepository;
        private final ProgressoAlunoRepository progressoAlunoRepository;

        public ProgressoResumoResponseDTO toDTO(List<ProgressoAluno> progressoes) {
            if (progressoes == null || progressoes.isEmpty()) {
                return null;
            }

            ProgressoResumoResponseDTO dto = new ProgressoResumoResponseDTO();
            Aluno aluno = progressoes.get(0).getAluno();

            // Mapeamento básico do aluno
            dto.setAlunoId(aluno.getId());
            dto.setAlunoNome(aluno.getNome());

            // Calcula totais consolidados
            long totalDisciplinas = progressoes.stream()
                    .flatMap(p -> p.getDisciplinas().stream())
                    .count();

            long disciplinasConcluidas = progressoes.stream()
                    .flatMap(p -> p.getDisciplinas().stream())
                    .filter(d -> d.getStatus() == StatusProgresso.DISCIPLINA_CONCLUIDA)
                    .count();

            long totalTopicos = progressoes.stream()
                    .flatMap(p -> p.getDisciplinas().stream())
                    .flatMap(d -> d.getTopicos().stream())
                    .count();

            long topicosConcluidos = progressoes.stream()
                    .flatMap(p -> p.getDisciplinas().stream())
                    .flatMap(d -> d.getTopicos().stream())
                    .filter(t -> t.getStatus() == StatusTopico.TOPICO_CONCLUIDO)
                    .count();

            // Preenche os valores
            dto.setTotalDisciplinas(totalDisciplinas);
            dto.setDisciplinasConcluidas(disciplinasConcluidas);
            dto.setTotalTopicos(totalTopicos);
            dto.setTopicosConcluidos(topicosConcluidos);

            // Calcula percentual geral
            if (totalDisciplinas > 0) {
                BigDecimal progresso = BigDecimal.valueOf(disciplinasConcluidas)
                        .divide(BigDecimal.valueOf(totalDisciplinas), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                dto.setProgressoGeral(progresso);
            } else {
                dto.setProgressoGeral(BigDecimal.ZERO);
            }

            return dto;
        }

     /*   public ProgressoResumoDTO toProgressoResumoDTO(Long alunoId) {
            Aluno aluno = alunoRepository.findById(alunoId)
                    .orElseThrow(() -> new ResourceNotfoundException("Aluno não encontrado"));

            List<ProgressoAluno> progressoes = progressoAlunoRepository.findByAlunoIdWithDetails(alunoId);
            return toDTO(progressoes);
        }

      */
}
