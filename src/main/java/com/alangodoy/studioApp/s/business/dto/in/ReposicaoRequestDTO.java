package com.alangodoy.studioApp.s.business.dto.in;

import com.alangodoy.studioApp.s.business.dto.in.aluno.AulaRequestDTO;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusReposicao;
import com.alangodoy.studioApp.s.infrastructure.enums.TipoAula;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReposicaoRequestDTO {

    private Long id;

    @NotNull(message = "Aluno é obrigatório")
    private Long aulaOriginalId;

    @JsonIgnore
    private AulaRequestDTO aulaReposicao;

    @Future(message = "Nova data deve ser no futuro")
    private LocalDateTime novaDataHora;

    private TipoAula tipoAula;


    @NotBlank(message = "Motivo é obrigatório")
    @Size(max = 500, message = "Motivo deve ter no máximo 500 caracteres")
    private String motivo;

    @Builder.Default
    private StatusReposicao status = StatusReposicao.PENDENTE;

    @Builder.Default
    private LocalDateTime dataSolicitacao = LocalDateTime.now();


    // Getters e Setters
}
