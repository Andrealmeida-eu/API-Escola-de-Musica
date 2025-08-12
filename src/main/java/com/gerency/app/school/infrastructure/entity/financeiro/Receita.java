package com.alangodoy.studioApp.s.infrastructure.entity.financeiro;

import com.alangodoy.studioApp.s.infrastructure.enums.TipoReceita;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "receitas")
@Getter
@Setter
@NoArgsConstructor
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "data_recebimento", nullable = false)
    private LocalDate dataRecebimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoReceita tipo;

    @Column(nullable = false)
    private String formaPagamento;

    @ManyToOne
    @JoinColumn(name = "mensalidade_id")
    private Mensalidade mensalidade;


}

