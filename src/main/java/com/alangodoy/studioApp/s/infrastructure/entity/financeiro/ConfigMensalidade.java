package com.alangodoy.studioApp.s.infrastructure.entity.financeiro;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "configuracoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigMensalidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor_mensalidade", nullable = false)
    private BigDecimal valorMensalidade;

    @Column(name = "dia_vencimento", nullable = false)
    private Integer diaVencimento; // Ex: 10 (para dia 10 de cada mês)

}
