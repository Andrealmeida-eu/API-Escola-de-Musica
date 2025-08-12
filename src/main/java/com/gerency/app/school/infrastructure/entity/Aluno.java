package com.alangodoy.studioApp.s.infrastructure.entity;


import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Mensalidade;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.ConteudoProgramatico;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Disciplina;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoAluno;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;


    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF inválido")
    private String cpf;

    @Column(unique = true, nullable = false)
    @Email(message = "E-mail inválido")
    private String email;

    private String telefone;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    @Builder.Default
    private LocalDate dataCadastro = LocalDate.now();


    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "instrumento_id", nullable = false)
    private Instrumento instrumento;

    @ManyToOne
    @JoinColumn(name = "conteudo_atual_id")
    private ConteudoProgramatico conteudoAtual;


    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProgressoAluno> progresso = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "aluno_disciplina",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "disciplina_id"))
    @Builder.Default
    private List<Disciplina> disciplinas = new ArrayList<>();


    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Aula> aulas = new HashSet<>();

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Mensalidade> mensalidades = new HashSet<>();

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDate.now();
    }



}