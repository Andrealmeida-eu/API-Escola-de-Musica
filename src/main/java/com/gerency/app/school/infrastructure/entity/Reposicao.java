package com.alangodoy.studioApp.s.infrastructure.entity;



import com.alangodoy.studioApp.s.infrastructure.enums.StatusReposicao;
import com.alangodoy.studioApp.s.infrastructure.enums.TipoAula;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "reposicoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reposicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_original_id", nullable = false)
    private Aula aulaOriginal;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "aula_reposicao_id", nullable = false)
    private Aula aulaReposicao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAula tipoAula;

    @Column(name = "nova_data_hora", nullable = true)
    private LocalDateTime novaDataHora;

    @Column(nullable = false)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusReposicao status = StatusReposicao.PENDENTE; // Valor inicial mais adequado

    @Column(name = "data_solicitacao", nullable = false)
    @Builder.Default
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Column(name = "data_realizacao")
    private LocalDateTime dataRealizacao;

    @Builder.Default
// Novo (com padrão Observer)
    @OneToMany(mappedBy = "reposicao", cascade = CascadeType.ALL)
    private List<HistoricoStatusReposicao> historico = new ArrayList<>();

    // Método para alterar status com histórico
    public void setStatus(StatusReposicao novoStatus) {
        if (this.status != novoStatus) {
            HistoricoStatusReposicao historico = new HistoricoStatusReposicao();
            historico.setDataAlteracao(LocalDateTime.now());
            historico.setStatusAnterior(this.status);
            historico.setNovoStatus(novoStatus);
            historico.setReposicao(this);
            this.historico.add(historico);

            this.status = novoStatus;

            if (novoStatus == StatusReposicao.REALIZADA) {
                this.dataRealizacao = LocalDateTime.now();
            }
        }
    }

}

