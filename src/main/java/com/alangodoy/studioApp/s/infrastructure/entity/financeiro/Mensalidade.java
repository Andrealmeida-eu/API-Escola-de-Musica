package com.alangodoy.studioApp.s.infrastructure.entity.financeiro;


import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusMensalidade;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mensalidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensalidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;


    @Column(nullable = false)
    private BigDecimal valor;


    // Novo (com regra de negócio e ordenação)
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    // Sempre será dia 10
    @Column(name = "data_pagamento")

    private LocalDate dataPagamento; // Só será incluído no JSON se não for nulo


    @Column(name = "data_ultima_utilização")
    private LocalDate dataUltimaAtualizacao;

    @Column(nullable = false)
    private Integer ano;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusMensalidade status = StatusMensalidade.ABERTA;

   @OneToMany(mappedBy = "mensalidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @Builder.Default
    private List<StatusMensalidadeHistorico> historicoStatus = new ArrayList<>();




}


