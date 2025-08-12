package com.alangodoy.studioApp.s.infrastructure.entity.financeiro;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria_despesas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDespesas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String descricao;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Despesas> despesas = new ArrayList<>();
}
