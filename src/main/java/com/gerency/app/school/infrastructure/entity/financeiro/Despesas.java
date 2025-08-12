package com.alangodoy.studioApp.s.infrastructure.entity.financeiro;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;



@Entity
@Table(name = "despesas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Despesas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private BigDecimal valor;
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private CategoriaDespesas categoria;
}
