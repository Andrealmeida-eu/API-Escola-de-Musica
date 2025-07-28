package com.alangodoy.studioApp.s.business.dto.in.aluno;

import com.alangodoy.studioApp.s.business.dto.in.instrumento.InstrumentoRequestDTO;
import jakarta.validation.constraints.NotNull;
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
public class AlunoRequestDTO {



    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank // @CPF
    private String cpf;


    @Email
    @NotBlank
    private String email;

    private String telefone;

    @NotNull
    private Long instrumentoId;

    private Long mensalidadeId;
    @Builder.Default
    private Set<AulaRequestDTO> aulas = new HashSet<>();

    @Builder.Default
    private Set<MensalidadeRequestDTO> mensalidades = new HashSet<>();
    private InstrumentoRequestDTO instrumento;

    private DayOfWeek diaSemanaAula;


    private LocalTime horarioAula;

    private ProfessorRequestDTO professor;


    @Builder.Default
    private LocalDate dataCadastro = LocalDate.now();
}