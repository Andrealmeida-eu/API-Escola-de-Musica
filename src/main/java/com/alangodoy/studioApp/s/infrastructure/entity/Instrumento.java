package com.alangodoy.studioApp.s.infrastructure.entity;


import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.ConteudoProgramatico;
import com.alangodoy.studioApp.s.infrastructure.enums.InstrumentoTipo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "instrumentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InstrumentoTipo tipo;

    @Builder.Default
    private Integer quantidadeDeAluno = 0; // Inicializa com 0

    @OneToMany(mappedBy = "instrumento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private Set<ConteudoProgramatico> conteudoProgramatico = new HashSet<>(); // Mudar para Set

    @Transient
    public ConteudoProgramatico getConteudoAtivo() {
        if (this.conteudoProgramatico == null || this.conteudoProgramatico.isEmpty()) {
            return null;
        }

        for (ConteudoProgramatico conteudo : this.conteudoProgramatico) {
            if (conteudo != null && conteudo.isAtivo()) {
                return conteudo;
            }
        }
        return null;
    }

    public void incrementarQuantidadeAlunos() { // Removi o parâmetro Aluno que não era usado
        if (this.quantidadeDeAluno == null) {
            this.quantidadeDeAluno = 0;
        }
        this.quantidadeDeAluno++;
    }

    public void decrementarQuantidadeAlunos() {
        if (this.quantidadeDeAluno == null || this.quantidadeDeAluno <= 0) {
            this.quantidadeDeAluno = 0;
            return;
        }
        this.quantidadeDeAluno--;
    }
}



