package com.alangodoy.studioApp.s.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "professor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String telefone;
/*
    @ManyToOne
    @JoinColumn(name = "instrumento_id")
    private Instrumento instrumento;

 */

    @ManyToMany
    @Builder.Default
    @JoinTable(
            name = "professor_instrumento",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "instrumento_id"))
    private Set<Instrumento> instrumentos = new HashSet<>();

    @OneToMany(mappedBy = "professor")
    private List<Aula> aulas;

}
