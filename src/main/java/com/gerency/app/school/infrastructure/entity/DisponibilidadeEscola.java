package com.alangodoy.studioApp.s.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidade_escola")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class DisponibilidadeEscola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private DayOfWeek diaSemana;

    private LocalTime horaInicio;

    private LocalTime horaFim;
    @Builder.Default
    private boolean ativo = true;
}
