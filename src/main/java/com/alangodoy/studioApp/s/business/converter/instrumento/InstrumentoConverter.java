package com.alangodoy.studioApp.s.business.converter.instrumento;


import com.alangodoy.studioApp.s.business.converter.conteudo.DisciplinaConverter;
import com.alangodoy.studioApp.s.business.dto.in.instrumento.InstrumentoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.DisciplinaResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.instrumento.InstrumentoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class InstrumentoConverter {

    private final DisciplinaConverter disciplinaConverter;

    public Instrumento toInstrumentoEntity(InstrumentoRequestDTO dto) {
        if (dto == null) return null;

        return Instrumento.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .tipo(dto.getTipo())
                .quantidadeDeAluno(dto.getQuantidadeDeAluno() != null ?
                        dto.getQuantidadeDeAluno() : 0)
                .build();
    }


        public InstrumentoResponseDTO toInstrumentoDTO(Instrumento entity){
            if (entity == null) return null;
            InstrumentoResponseDTO dto = new InstrumentoResponseDTO();
            dto.setId(entity.getId());
            dto.setNome(entity.getNome());
            dto.setTipo(entity.getTipo());
            dto.setQuantidadeDeAluno(entity.getQuantidadeDeAluno());

            // Verifica se ConteudoProgramatico existe antes de mapear as disciplinas
            if (entity.getConteudoAtivo() != null) {
                // Verifica se a lista de disciplinas existe para evitar NPE
                List<DisciplinaResponseDTO> disciplinasDTO = Optional.ofNullable(entity.getConteudoAtivo().getDisciplinas())
                        .orElse(Collections.emptySet())
                        .stream()
                        .map(disciplinaConverter::toDisciplinaDTO)
                        .toList();

                dto.setDisciplinas(disciplinasDTO);
            } else {
                // Se ConteudoProgramatico for null, define disciplinas como lista vazia
                dto.setDisciplinas(Collections.emptyList());
            }


            return dto;
        }




        public List<InstrumentoResponseDTO> toDTOList (List < Instrumento > instrumentos) {
            return instrumentos.stream()
                    .map(this::toInstrumentoDTO)
                    .collect(Collectors.toList());
        }




 }







