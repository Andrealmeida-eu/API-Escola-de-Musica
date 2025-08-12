package com.alangodoy.studioApp.s.business.converter.progresso;

import com.alangodoy.studioApp.s.business.converter.conteudo.DisciplinaConverter;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoAlunoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoDetalhadoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoResumoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoTopicoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoAluno;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoDisciplina;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoTopico;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusTopico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProgressoConverter {

    private final DisciplinaConverter disciplinaConverter;


    public ProgressoAlunoResponseDTO toProgressoAlunoDTO(ProgressoAluno entity) {
        if (entity == null) {
            return null;
        }

        ProgressoAlunoResponseDTO dto = new ProgressoAlunoResponseDTO();
        dto.setId(entity.getId());

        // Mapeamento do Aluno
        if (entity.getAluno() != null) {
            dto.setAlunoId(entity.getAluno().getId());
            dto.setAlunoNome(entity.getAluno().getNome());
        }

        // Mapeamento do Instrumento
        if (entity.getInstrumento() != null) {
            dto.setInstrumentoId(entity.getInstrumento().getId());
            dto.setInstrumentoNome(entity.getInstrumento().getNome());
        }

        // Mapeamento dos campos básicos
        dto.setStatus(entity.getStatus());
        dto.setDataInicio(entity.getDataInicio());
        dto.setUltimaAtualizacao(LocalDate.from(entity.getUltimaAtualizacao()));
        dto.setPercentualConclusao(entity.getPercentualConclusao());

        // Mapeamento das disciplinas
        if (entity.getDisciplinas() != null) {
            dto.setDisciplinas(entity.getDisciplinas().stream()
                    .map(disciplinaConverter::toDisciplinaPoDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }


/*
    public ProgressoDisciplinaDTO toProgressoDisciplinaDTO(Disciplina disciplina,
                                                           Map<Long, ProgressoTopico> progressaoMap) {
        ProgressoDisciplinaDTO dto = new ProgressoDisciplinaDTO();
        dto.setDisciplinaId(disciplina.getId());
        dto.setDisciplinaNome(disciplina.getNome());

        // Mapeia os tópicos com sua progressão correspondente
        List<ProgressoTopicoDTO> topicosDTO = disciplina.getTopicos().stream()
                .map(topico -> {
                    ProgressoTopico progresso = progressaoMap.get(topico.getId());
                    return toProgressoTopicoDTO(topico, progresso);
                })
                .collect(Collectors.toList());

        dto.setTopicos(topicosDTO);
        dto.setTotal(disciplina.getTopicos().size());
        dto.setConcluidos((int) topicosDTO.stream()
                .filter(t -> t.getStatus() == StatusTopico.TOPICO_CONCLUIDO)
                .count());

        // Calcula progresso percentual
        if (dto.getTotal() > 0) {
            dto.setProgresso(
                    BigDecimal.valueOf(dto.getConcluidos())
                            .divide(BigDecimal.valueOf(dto.getTotal()), 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
            );
        } else {
            dto.setProgresso(BigDecimal.ZERO);
        }

        return dto;
    }
*/

    public ProgressoTopicoResponseDTO toProgressoTopicoDTO(ProgressoTopico progressao) {
        ProgressoTopicoResponseDTO dto = new ProgressoTopicoResponseDTO();

        dto.setId(progressao.getId());
        dto.setTopicoId(progressao.getTopico().getId());
        dto.setOrdem(progressao.getTopico().getOrdem());
        dto.setTopicoNome(progressao.getTopico().getNome());


        if (progressao != null) {
            dto.setStatus(progressao.getStatus());
            dto.setDataInicio(progressao.getDataInicio());
            dto.setDataConclusao(progressao.getDataConclusao());
            dto.setConcluido(progressao.isConcluido());

            dto.setProgresso(progressao.getProgresso());

        } else {
            dto.setStatus(StatusTopico.TOPICO_NAO_INICIADO);
        }

        return dto;
    }

    // vamos atrualizar

        public ProgressoDetalhadoResponseDTO toProgressoDetalhadoDTO(List<ProgressoAluno> progressos) {
            if (progressos == null || progressos.isEmpty()) {
                return null;
            }

            ProgressoDetalhadoResponseDTO dto = new ProgressoDetalhadoResponseDTO();
            dto.setAlunoId(progressos.get(0).getAluno().getId());
            dto.setAlunoNome(progressos.get(0).getAluno().getNome());

            List<ProgressoDetalhadoResponseDTO.ProgressoInstrumentoDTO> instrumentosDTO = progressos.stream()
                    .map(this::toInstrumentoProgressoDTO)
                    .collect(Collectors.toList());

            dto.setInstrumentos(instrumentosDTO);
            return dto;
        }

        private ProgressoDetalhadoResponseDTO.ProgressoInstrumentoDTO toInstrumentoProgressoDTO(ProgressoAluno progresso) {
            return ProgressoDetalhadoResponseDTO.ProgressoInstrumentoDTO.builder()
                    .instrumentoId(progresso.getInstrumento().getId())
                    .instrumentoNome(progresso.getInstrumento().getNome())
                    .progressoGeral(progresso.getProgressoGeral())
                    .disciplinas(toProgressoDisciplinasDTO(progresso.getDisciplinas()))
                    .build();
        }

        public List<ProgressoDetalhadoResponseDTO.ProgressoDisciplinaDTO> toProgressoDisciplinasDTO(List<ProgressoDisciplina> disciplinas) {
            return disciplinas.stream()
                    .map(this::toProgressoDisciplinaDTO)
                    .collect(Collectors.toList());
        }


        public ProgressoDetalhadoResponseDTO.ProgressoDisciplinaDTO toProgressoDisciplinaDTO(ProgressoDisciplina disciplina) {
            return ProgressoDetalhadoResponseDTO.ProgressoDisciplinaDTO.builder()
                    .disciplinaId(disciplina.getDisciplina().getId())
                    .disciplinaNome(disciplina.getDisciplina().getNome())
                    .progresso(disciplina.getProgresso())
                    .concluida(disciplina.isConcluido())
                    .topicos(toProgressoTopicosDTO(disciplina.getTopicos()))
                    .build();
        }

        private List<ProgressoDetalhadoResponseDTO.ProgressoTopicoDTO> toProgressoTopicosDTO(List<ProgressoTopico> topicos) {
            return topicos.stream()
                    .map(this::toTopicoProgressoDTO)
                    .collect(Collectors.toList());
        }

        private ProgressoDetalhadoResponseDTO.ProgressoTopicoDTO toTopicoProgressoDTO(ProgressoTopico topico) {
            return ProgressoDetalhadoResponseDTO.ProgressoTopicoDTO.builder()
                    .topicoId(topico.getTopico().getId())
                    .topicoNome(topico.getTopico().getNome())
                    .concluido(topico.isConcluido())
                    .dataConclusao(topico.getDataConclusao())
                    .observacoes(topico.getObservacoes())
                    .build();
        }

        public ProgressoResumoResponseDTO toProgressoResumoDTO(List<ProgressoAluno> progressos) {
            if (progressos == null || progressos.isEmpty()) {
                return null;
            }

            ProgressoResumoResponseDTO resumo = new ProgressoResumoResponseDTO();
            resumo.setAlunoId(progressos.get(0).getAluno().getId());
            resumo.setAlunoNome(progressos.get(0).getAluno().getNome());

            long totalDisciplinas = progressos.stream()
                    .flatMap(p -> p.getDisciplinas().stream())
                    .count();

            long disciplinasConcluidas = progressos.stream()
                    .flatMap(p -> p.getDisciplinas().stream())
                    .filter(d -> d.isConcluido())
                    .count();

            long totalTopicos = progressos.stream()
                    .flatMap(p -> p.getDisciplinas().stream())
                    .flatMap(d -> d.getTopicos().stream())
                    .count();

            long topicosConcluidos = progressos.stream()
                    .flatMap(p -> p.getDisciplinas().stream())
                    .flatMap(d -> d.getTopicos().stream())
                    .filter(t -> t.isConcluido())
                    .count();

            resumo.setTotalDisciplinas(totalDisciplinas);
            resumo.setDisciplinasConcluidas(disciplinasConcluidas);
            resumo.setTotalTopicos(totalTopicos);
            resumo.setTopicosConcluidos(topicosConcluidos);

            BigDecimal progressoGeral = totalDisciplinas > 0 ?
                    BigDecimal.valueOf(disciplinasConcluidas)
                            .divide(BigDecimal.valueOf(totalDisciplinas), 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100)) :
                    BigDecimal.ZERO;

            resumo.setProgressoGeral(progressoGeral);

            return resumo;
        }


}


