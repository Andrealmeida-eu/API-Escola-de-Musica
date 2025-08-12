package com.alangodoy.studioApp.s.business.converter.conteudo;

import com.alangodoy.studioApp.s.business.dto.in.conteudo.ConteudoProgramaticoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.in.conteudo.DisciplinaRequestDTO;
import com.alangodoy.studioApp.s.business.dto.in.instrumento.InstrumentoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.ConteudoProgramaticoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.DisciplinaResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.TopicoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.ConteudoProgramatico;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Disciplina;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Topico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConteudoProgramaticoConverter {

    private final DisciplinaConverter disciplinaConverter;



    public ConteudoProgramatico toCPEntity(ConteudoProgramaticoRequestDTO dto, Instrumento instrumento) {
        ConteudoProgramatico conteudo = new ConteudoProgramatico();
        conteudo.setInstrumento(instrumento);

        Set<Disciplina> disciplinas = dto.getDisciplinas().stream()
                .map(disciplinaDTO -> {
                    Disciplina disciplina = new Disciplina();
                    disciplina.setNome(disciplinaDTO.getNome());
                    disciplina.setDescricao(disciplinaDTO.getDescricao());
                    disciplina.setOrdem(disciplinaDTO.getOrdem());
                    disciplina.setConteudo(conteudo);

                    disciplina.setTopicos(disciplinaDTO.getTopicos().stream()
                            .map(topicoDTO -> {
                                Topico topico = new Topico();
                                topico.setNome(topicoDTO.getNome());
                                topico.setOrdem(topicoDTO.getOrdem());
                                topico.setDisciplina(disciplina);
                                return topico;
                            })
                            .collect(Collectors.toSet()));

                    return disciplina;
                })
                .collect(Collectors.toSet());

        conteudo.setDisciplinas(disciplinas);
        return conteudo;
    }

    public ConteudoProgramaticoResponseDTO toCPDTO(ConteudoProgramatico conteudo) {
        ConteudoProgramaticoResponseDTO dto = new ConteudoProgramaticoResponseDTO();
dto.setId(conteudo.getId());

        // Se o instrumento tem ID ou algum campo relevante para o DTO
        if (conteudo.getInstrumento() != null) {
            dto.setInstrumentoNome(conteudo.getInstrumento().getNome());
            // Ou pode criar um InstrumentoDTO simples se necessário
        }




        // Convertendo as disciplinas
        List<DisciplinaResponseDTO> disciplinaDTOs = conteudo.getDisciplinas().stream()
                .map(disciplina -> {
                    DisciplinaResponseDTO disciplinaDTO = new DisciplinaResponseDTO();
                    disciplinaDTO.setNome(disciplina.getNome());
                    disciplinaDTO.setDescricao(disciplina.getDescricao());
                    disciplinaDTO.setOrdem(disciplina.getOrdem());



                    // Convertendo os tópicos
                    Set<TopicoResponseDTO> topicoDTOs = disciplina.getTopicos().stream()
                            .map(topico -> {
                                TopicoResponseDTO topicoDTO = new TopicoResponseDTO();
                                topicoDTO.setNome(topico.getNome());
                                topicoDTO.setOrdem(topico.getOrdem());
                                return topicoDTO;
                            })
                            .collect(Collectors.toSet());

                    disciplinaDTO.setTopicos(topicoDTOs);
                    return disciplinaDTO;
                })
                .collect(Collectors.toList());

        dto.setDisciplinas(disciplinaDTOs);
        return dto;
    }

    private InstrumentoRequestDTO toInstrumentoRequestDTO(Instrumento instrumento) {
        InstrumentoRequestDTO dto = new InstrumentoRequestDTO();
        // Mapeie todos os campos necessários
        dto.setId(instrumento.getId());
        dto.setNome(instrumento.getNome());
        dto.setTipo(instrumento.getTipo());
        dto.setQuantidadeDeAluno(instrumento.getQuantidadeDeAluno());

        // ... outros campos
        return dto;
    }

    public Instrumento toInstrumentoRequestEntity(InstrumentoRequestDTO dto) {
        return Instrumento.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                // .tipo(dto.getTipo())
                // .fabricante(dto.getFabricante())
                .build();
    }

  /*
    public ConteudoProgramaticoResponseDTO toConteudoProgramaticoResponseDTO(ConteudoProgramatico conteudo) {
        ConteudoProgramaticoResponseDTO dto = new ConteudoProgramaticoResponseDTO();
        dto.setInstrumentoId(conteudo.getInstrumento().getId());
        dto.setNomeInstrumento(conteudo.getInstrumento().getNome());

     dto.setDisciplinas(conteudo.getDisciplinas().stream()
                .map(disciplina -> {
                    DisciplinaRequestDTO disciplinaDTO = new DisciplinaRequestDTO();
                    disciplinaDTO.setNome(disciplina.getNome());

                    disciplinaDTO.setTopicos(disciplina.getTopicos().stream()
                            .map(topico -> {
                                TopicoRequestDTO topicoDTO = new TopicoRequestDTO();
                                topicoDTO.setNome(topico.getNome());
                                return topicoDTO;
                            })
                            .collect(Collectors.toList()));

                    return disciplinaDTO; })



                .collect(Collectors.toList()));


        return dto;
    }                .collect(Collectors.toList()));
  */





        public void updateFromDTO(ConteudoProgramaticoRequestDTO dto, ConteudoProgramatico entity) {

            entity.setInstrumento(toInstrumentoRequestEntity(dto.getInstrumento()));

            // Atualiza disciplinas existentes ou adiciona novas
            Map<Long, DisciplinaRequestDTO> dtoDisciplinas = dto.getDisciplinas().stream()
                    .filter(d -> d.getId() != null)
                    .collect(Collectors.toMap(DisciplinaRequestDTO::getId, Function.identity()));

            // Atualiza ou remove disciplinas
            Iterator<Disciplina> iterator = entity.getDisciplinas().iterator();
            while (iterator.hasNext()) {
                Disciplina disciplina = iterator.next();
                if (dtoDisciplinas.containsKey(disciplina.getId())) {
                    updateDisciplinaFromDTO(dtoDisciplinas.get(disciplina.getId()), disciplina);
                    dtoDisciplinas.remove(disciplina.getId());
                } else {
                    iterator.remove();
                }
            }

            // Adiciona novas disciplinas
            dto.getDisciplinas().stream()
                    .filter(d -> d.getId() == null)
                    .map(disciplinaConverter::toDisciplinaEntity)
                    .forEach(entity.getDisciplinas()::add);
        }

        private void updateDisciplinaFromDTO(DisciplinaRequestDTO dto, Disciplina entity) {
            entity.setNome(dto.getNome());
            entity.setDescricao(dto.getDescricao());

            // Lógica similar para atualizar tópicos
            // ... implementação similar à das disciplinas
        }

}
