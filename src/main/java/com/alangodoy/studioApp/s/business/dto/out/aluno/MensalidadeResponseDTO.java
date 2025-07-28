package com.alangodoy.studioApp.s.business.dto.out.aluno;

import com.alangodoy.studioApp.s.infrastructure.enums.StatusMensalidade;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensalidadeResponseDTO {

    private Long id;
    private AlunoResponseDTO aluno;
    private BigDecimal valor;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataVencimento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dataPagamento;
    @Builder.Default
    private StatusMensalidade status = StatusMensalidade.ABERTA;

    @JsonGetter("dataPagamento")
    public String getDataPagamentoFormatada() {
        return dataPagamento != null
                ? dataPagamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "Aguardando Pagamento";
    }

    // Getters e Setters
}