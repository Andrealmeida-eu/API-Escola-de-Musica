package com.alangodoy.studioApp.s.controller;

import com.alangodoy.studioApp.s.business.dto.out.DisponibilidadeEmLoteDTO;
import com.alangodoy.studioApp.s.business.dto.out.HorarioDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.DisponibilidadeEscola;
import com.alangodoy.studioApp.s.infrastructure.repository.DisponibilidadeEscolaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/disponibilidades-escola")
public class DisponibilidadeEscolaController {

    @Autowired
    private DisponibilidadeEscolaRepository repository;

    @PostMapping("/lote")
    public ResponseEntity<Void> cadastrarEmLote(@RequestBody DisponibilidadeEmLoteDTO dto) {
        List<DisponibilidadeEscola> disponibilidades = new ArrayList<>();

        for (DayOfWeek dia : dto.getDiasSemana()) {
            for (HorarioDTO horario : dto.getHorarios()) {
                DisponibilidadeEscola disponibilidade = new DisponibilidadeEscola();
                disponibilidade.setDiaSemana(dia);
                disponibilidade.setHoraInicio(horario.getHoraInicio());
                disponibilidade.setHoraFim(horario.getHoraFim());
                disponibilidade.setAtivo(true);

                disponibilidades.add(disponibilidade);
            }
        }

        repository.saveAll(disponibilidades);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping
    public List<DisponibilidadeEscola> listar() {
        return repository.findByAtivoTrue();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        DisponibilidadeEscola disponibilidade = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        disponibilidade.setAtivo(false);
        repository.save(disponibilidade);
        return ResponseEntity.noContent().build();
    }
}
