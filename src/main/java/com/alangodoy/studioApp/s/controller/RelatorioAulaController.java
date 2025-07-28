package com.alangodoy.studioApp.s.controller;

import com.alangodoy.studioApp.s.business.dto.in.RelatorioAulaRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.RelatorioAulaResponseDTO;
import com.alangodoy.studioApp.s.business.services.RelatorioAulaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/escola-musica/relatorios")
public class RelatorioAulaController {


    private final RelatorioAulaService relatorioService;


    @PostMapping
    public ResponseEntity<RelatorioAulaResponseDTO> criarRelatorio(
            @Valid @RequestBody RelatorioAulaRequestDTO dto) {

        RelatorioAulaResponseDTO response = relatorioService.criarRelatorio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RelatorioAulaResponseDTO>> listarRelatorios(
            @RequestParam(required = false) Long professorId,
            @RequestParam(required = false) Long alunoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<RelatorioAulaResponseDTO> relatorios = relatorioService.listarRelatorios(
                professorId, alunoId, dataInicio, dataFim);

        return ResponseEntity.ok(relatorios);
    }

    @PatchMapping("/{id}/parcial")
    public ResponseEntity<RelatorioAulaResponseDTO> atualizarRelatorioParcialmente(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(relatorioService.atualizarParcialmente(id, updates));
    }

   /*@GetMapping("/{id}")
    public ResponseEntity<RelatorioAulaDTO> buscarPorAlunoId(@PathVariable Long alunoId) {
        return ResponseEntity.ok(relatorioAulaRepository.findByAlunoId(alunoId));
    }*/
}
