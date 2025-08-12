package com.alangodoy.studioApp.s.business.dto.in.aluno;

import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfessorRequestDTO {

    private Long id;

    @NotBlank @Size(max = 100)
    private String nome;

    @NotBlank // @CPF
    private String cpf;

    @Email @NotBlank
    private String email;

   // @Pattern(regexp = "...")
    private String telefone;
    @Builder.Default
    private Set<Instrumento> instrumentos = new HashSet<>();
    private Long instrumentoId;
    @Builder.Default
    private Set<Long> instrumentosIds = new HashSet<>();
}
