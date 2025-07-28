package com.alangodoy.studioApp.s.business.converter.aluno;

import com.alangodoy.studioApp.s.business.converter.instrumento.InstrumentoConverter;
import com.alangodoy.studioApp.s.business.dto.in.aluno.AlunoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.AlunoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.Aula;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AlunoConverter {


    private final AulaConverter aulaConverter;
    private final MensalidadeConverter mensalidadeConverter;
    private final InstrumentoConverter instrumentoConverter;
    private final ProfessorConverter professorConverter;

    public Aluno toEntity(AlunoRequestDTO dto) {
        if (dto == null) return null;

        return Aluno.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .dataCadastro(dto.getDataCadastro())
                .build();
    }

    public AlunoResponseDTO toDTO(Aluno entity) {
        if (entity == null) return null;

        AlunoResponseDTO dto = new AlunoResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCpf(entity.getCpf());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setDataCadastro(entity.getDataCadastro());


        // Conversões aninhadas usando os outros converters
        if (entity.getInstrumento() != null) {
            dto.setInstrumento(instrumentoConverter.toInstrumentoDTO(entity.getInstrumento()));
        }



        if (entity.getProfessor() != null) {
            dto.setProfessor(professorConverter.toDTO(entity.getProfessor()));
        }

        dto.setMensalidades(entity.getMensalidades().stream()
                .map(mensalidadeConverter::toDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));

        if (entity.getAulas() != null && !entity.getAulas().isEmpty()) {
            dto.setAulas(entity.getAulas().stream()
                    .map(aulaConverter::toDTO)
                    .collect(Collectors.toSet()));

            Aula primeiraAula = entity.getAulas().stream().findFirst().orElse(null);
            if (primeiraAula != null) {
                dto.setDiaSemanaAula(primeiraAula.getDiaSemanaAula());
                dto.setHorarioAula(primeiraAula.getHorarioAula());
            }
        }

        return dto;
    }

    public void updateAlunoDTO(AlunoResponseDTO dto, Aluno entity) {
        dto.setNome(entity.getNome());
        dto.setCpf(entity.getCpf());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());


        // Conversões aninhadas usando os outros converters
        if (entity.getInstrumento() != null) {
            dto.setInstrumento(instrumentoConverter.toInstrumentoDTO(entity.getInstrumento()));
        }

        if (entity.getMensalidades() != null) {
            dto.setMensalidades(entity.getMensalidades().stream()
                    .map(mensalidadeConverter::toDTO)
                    .collect(Collectors.toSet()));
        }

        if (entity.getProfessor() != null) {
            dto.setProfessor(professorConverter.toDTO(entity.getProfessor()));
        }

        if (entity.getAulas() != null && !entity.getAulas().isEmpty()) {
            dto.setAulas(entity.getAulas().stream()
                    .map(aulaConverter::toDTO)
                    .collect(Collectors.toSet()));

            Aula aula = new Aula();
            dto.setDiaSemanaAula(aula.getDiaSemanaAula());
            dto.setHorarioAula(aula.getHorarioAula());
        }

    }

}
