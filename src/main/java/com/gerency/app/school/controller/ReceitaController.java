package com.alangodoy.studioApp.s.controller;

import com.alangodoy.studioApp.s.business.dto.out.receita.ReceitaResponseDTO;
import com.alangodoy.studioApp.s.business.services.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/escola-musica/receita")
public class ReceitaController {

    private final ReceitaService receitaService;

    @GetMapping
    public ResponseEntity<ReceitaResponseDTO> calcularReceitaEscola(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(receitaService.calcularReceita(inicio, fim));
    }

    @GetMapping("pagas")
    public ResponseEntity<Long> contarPagasEscola(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(receitaService.contarMensalidadesPagas(inicio, fim));
    }

    @GetMapping("nao-pagas")
    public ResponseEntity<Long> contarNaoPagasEscola(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(receitaService.contarMensalidadesAbertas(inicio, fim));
    }
}
