package com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade;

import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConteudoProgramatico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "instrumento_id", nullable = false)
    @JsonIgnore
    private Instrumento instrumento;

    @Builder.Default
    @OneToMany(mappedBy = "conteudo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Isso evita que a referência circular seja serializada
    private Set<Disciplina> disciplinas = new HashSet<>(); // Mudar para Set

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;


    // Getters e Setters
}

