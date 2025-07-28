package com.alangodoy.studioApp.s.business.dto.out;

import com.alangodoy.studioApp.s.infrastructure.entity.Aula;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusReposicao;
import com.alangodoy.studioApp.s.infrastructure.enums.TipoAula;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReposicaoResponseDTO {

    private Long id;
    @JsonIgnore
    private Aula aulaReposicao;


    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime novaDataHora;
    private String motivo;

    @Builder.Default
    private StatusReposicao status = StatusReposicao.PENDENTE;

    private TipoAula tipoAula;

    @Builder.Default
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataHoraAulaOriginal;

    private String alunoNome;


    // Getters e Setters
}
