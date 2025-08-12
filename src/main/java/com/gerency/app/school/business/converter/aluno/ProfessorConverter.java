package com.alangodoy.studioApp.s.business.converter.aluno;

import com.alangodoy.studioApp.s.business.converter.instrumento.InstrumentoConverter;
import com.alangodoy.studioApp.s.business.dto.out.aluno.ProfessorResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.alangodoy.studioApp.s.infrastructure.entity.Professor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfessorConverter {

    private final InstrumentoConverter instrumentoConverter;

    public Professor toEntity(ProfessorResponseDTO dto) {
        if (dto == null) return null;

        return Professor.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .build();
    }

    public ProfessorResponseDTO toDTO(Professor entity) {
        if (entity == null) return null;

        ProfessorResponseDTO dto = new ProfessorResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCpf(entity.getCpf());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        // Aqui está o ponto crucial:
        dto.setInstrumentos(
                entity.getInstrumentos().stream()
                        .map(instrumentoConverter::toInstrumentoDTO) // transforma a entidade em DTO
                        .collect(Collectors.toSet())
        );
        dto.setInstrumentosIds(entity.getInstrumentos().stream()
                .map(Instrumento::getId)
                .collect(Collectors.toSet()));


        return dto;
    }


    public List<ProfessorResponseDTO> toDTOList(List<Professor> professores) {
        return professores.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

