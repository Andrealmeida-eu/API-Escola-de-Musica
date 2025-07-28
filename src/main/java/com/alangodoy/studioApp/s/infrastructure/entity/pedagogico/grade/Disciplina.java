package com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "disciplina")  // Nome explícito da tabela
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome; // "tecnicaPratica", "teoria", etc.

    @ManyToOne
    @JoinColumn(name = "conteudo_id", nullable = false)
    @JsonIgnore
    private ConteudoProgramatico conteudo; // Relação com ConteudoProgramatico

    // Relação com Tópicos - Estratégia 2 (recomendada)
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem ASC")
    @Builder.Default
    @JsonIgnore // Isso evita que a referência circular seja serializada
    private Set<Topico> topicos = new HashSet<>();

    @Column(length = 1000)
    private String descricao;

    @Column
    private Boolean conluida;


    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;


    // Getters e Setters
}
