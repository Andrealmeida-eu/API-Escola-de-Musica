package com.alangodoy.studioApp.s.business.dto.out.progresso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressoDetalhadoResponseDTO {
    private Long alunoId;
    private String alunoNome;
    private List<ProgressoInstrumentoDTO> instrumentos;

    @Data
    @Builder
    public static class ProgressoInstrumentoDTO {
        private Long instrumentoId;
        private String instrumentoNome;
        private BigDecimal progressoGeral;
        private List<ProgressoDisciplinaDTO> disciplinas;
    }

    @Data
    @Builder
    public static class ProgressoDisciplinaDTO {
        private Long disciplinaId;
        private String disciplinaNome;
        private BigDecimal progresso;
        private boolean concluida;
        private List<ProgressoTopicoDTO> topicos;
    }

    @Data
    @Builder
    public static class ProgressoTopicoDTO {
        private Long topicoId;
        private String topicoNome;
        private boolean concluido;

        private LocalDate dataConclusao;
        private String observacoes;
    }
}