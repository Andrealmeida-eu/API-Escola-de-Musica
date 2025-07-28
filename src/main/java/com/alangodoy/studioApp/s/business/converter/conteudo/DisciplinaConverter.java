package com.alangodoy.studioApp.s.business.converter.conteudo;

import com.alangodoy.studioApp.s.business.dto.in.conteudo.DisciplinaRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.DisciplinaResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoDisciplinaResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoTopicoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Disciplina;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoDisciplina;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoTopico;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusTopico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DisciplinaConverter {

    private final TopicoConverter topicoConverter;


    public DisciplinaResponseDTO toDisciplinaDTO(Disciplina disciplina) {
        DisciplinaResponseDTO dto = new DisciplinaResponseDTO();
        dto.setId(disciplina.getId());
        dto.setNome(disciplina.getNome());
        dto.setOrdem(disciplina.getOrdem());
        dto.setDescricao(disciplina.getDescricao());

        // Converter os tópicos
        if (disciplina.getTopicos() != null) {
            dto.setTopicos(disciplina.getTopicos().stream()
                    .map(topicoConverter::toTopicoDTO)
                    .collect(Collectors.toSet()));
        }
     /*   dto.setTopicos(disciplina.getTopicos().stream()
                .map(topicoConverter::toTopicoDTO)
                .collect(Collectors.toList()));

      */

        return dto;


    }

   /* public ProgressoDisciplinaDTO toDisciplinaPoDTO(ProgressoDisciplina disciplina) {
        if (disciplina == null) {
            return null;
        }
        Map<Long, ProgressoTopico> progressaoMap = Map.of();
        ProgressoTopicoDTO dtoT = new ProgressoTopicoDTO();
        
        ProgressoDisciplinaDTO dto = new ProgressoDisciplinaDTO();
        dto.setDisciplinaId(disciplina.getId());
        dto.setDisciplinaNome(disciplina.getDisciplina().getNome());

        List<ProgressoTopicoDTO> topicosDTO = disciplina.getTopicos().stream()
                .map(topico -> {
                    ProgressoTopico progresso = progressaoMap.get(topico.getId());
                    return progressoMap.toProgressoTopicoDTO(topico.getTopico(), progresso);
                })
                .collect(Collectors.toList());

        dto.setTopicos(topicosDTO);
        dto.setTotal(disciplina.getTopicos().size());
      
        dto.setConcluidos((int) topicosDTO.stream()
                .filter(t -> t.getStatus() == StatusTopico.TOPICO_CONCLUIDO)
                .count());

        // Map other fields as necessary

        return dto;
    }*/
   public ProgressoDisciplinaResponseDTO toDisciplinaPoDTO(ProgressoDisciplina disciplina) {
       if (disciplina == null) {
           return null;
       }

       ProgressoDisciplinaResponseDTO dto = new ProgressoDisciplinaResponseDTO();
       dto.setDisciplinaId(disciplina.getDisciplina().getId());  // Usar ID da disciplina, não do progresso
       dto.setDisciplinaNome(disciplina.getDisciplina().getNome());
       dto.setId(disciplina.getId());  // ID do progresso da disciplina

       // Mapear status e conclusão
       dto.setStatus(disciplina.getStatus());
       dto.setConcluida(disciplina.isConcluido());
       dto.setProgresso(disciplina.getProgresso());

       // Mapear datas
       dto.setDataInicio(disciplina.getDataInicio());
       dto.setDataConclusao(disciplina.getDataConclusao());

       // Mapear tópicos
       List<ProgressoTopicoResponseDTO> topicosDTO = disciplina.getTopicos().stream()
               .map(this::toTopicoDTO)  // Usar método auxiliar para converter tópicos
               .collect(Collectors.toList());

       dto.setTopicos(topicosDTO);
       dto.setTotal(disciplina.getTopicos().size());

       dto.setConcluidos((int) topicosDTO.stream()
               .filter(t -> t.getStatus() == StatusTopico.TOPICO_CONCLUIDO)
               .count());

       return dto;
   }

    // Método auxiliar para converter ProgressoTopico para DTO
    private ProgressoTopicoResponseDTO toTopicoDTO(ProgressoTopico topico) {
        ProgressoTopicoResponseDTO dto = new ProgressoTopicoResponseDTO();
        dto.setId(topico.getId());
        dto.setTopicoNome(topico.getTopico().getNome());
        dto.setTopicoId(topico.getTopico().getId());
        dto.setStatus(topico.getStatus());
        dto.setConcluido(topico.isConcluido());
        dto.setDataInicio(topico.getDataInicio());
        dto.setDataConclusao(topico.getDataConclusao());
        dto.setProgresso(topico.getProgresso());
        return dto;
    }

    public Disciplina toDisciplinaEntity(DisciplinaRequestDTO dto) {
        Disciplina disciplina = new Disciplina();
        disciplina.setNome(dto.getNome());
        disciplina.setDescricao(dto.getDescricao());

        disciplina.setTopicos(dto.getTopicos().stream()
                .map(topicoConverter::toTopicoEntity)
                .collect(Collectors.toSet()));

        return disciplina;
    }
}
