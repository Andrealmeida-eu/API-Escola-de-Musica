package com.alangodoy.studioApp.s.business.dto.out.aluno;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ConfigResponseDTO {

    private BigDecimal valorMensalidade;
    private Integer diaVencimento;


}
