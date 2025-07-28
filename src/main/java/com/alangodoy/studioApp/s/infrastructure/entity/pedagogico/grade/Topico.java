package com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "topicos")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer ordem;

    @Column(length = 1000)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false, foreignKey = @ForeignKey(name = "fk_topico_disciplina"))
    private Disciplina disciplina;

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;


}
