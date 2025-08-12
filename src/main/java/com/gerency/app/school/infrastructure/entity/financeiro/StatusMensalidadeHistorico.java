package com.alangodoy.studioApp.s.infrastructure.entity.financeiro;

import com.alangodoy.studioApp.s.infrastructure.enums.StatusMensalidade;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusMensalidadeHistorico {

    @Id
    @GeneratedValue
    private Long id;
    private StatusMensalidade statusAnterior;
    private StatusMensalidade statusNovo;
    private LocalDate dataModificacao;
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "mensalidade_id")
    private Mensalidade mensalidade;
}
