package com.alangodoy.studioApp.s.business.dto.out.aluno;

import com.alangodoy.studioApp.s.business.dto.out.instrumento.InstrumentoResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlunoResponseDTO {

    private Long id;
    @NotBlank
    @Size(max = 100)
    private String nome;
    @NotBlank // @CPF
    private String cpf;
    @Email
    @NotBlank
    private String email;
    private String telefone;


    @Builder.Default
    private Set<AulaResponseDTO> aulas = new HashSet<>();

    @Builder.Default
    private Set<MensalidadeResponseDTO> mensalidades = new HashSet<>();

    private InstrumentoResponseDTO instrumento;

    private DayOfWeek diaSemanaAula;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioAula;

    private ProfessorResponseDTO professor;

    @Builder.Default
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCadastro = LocalDate.now();


}