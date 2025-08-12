package com.alangodoy.studioApp.s.controller;

import com.alangodoy.studioApp.s.business.dto.in.receita.DespesasRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.receita.DespesasResponseDTO;
import com.alangodoy.studioApp.s.business.services.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/escola-musica/despesas")
@RequiredArgsConstructor
public class DespesasController {
    private final DespesaService despesaService;


    @PostMapping
    public DespesasResponseDTO criarDespesa(@RequestBody DespesasRequestDTO despesasDTO) {
        return despesaService.criarDespesa(despesasDTO);
    }

    @GetMapping
    public List<DespesasResponseDTO> listarTodasDespesas() {
        return despesaService.listarTodas();
    }

    @GetMapping("/periodo")
    public List<DespesasResponseDTO> listarDespesasPorPeriodo(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {
        return despesaService.listarPorPeriodo(inicio, fim);
    }

    @GetMapping("/total-periodo")
    public BigDecimal calcularTotalDespesasPeriodo(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {
        return despesaService.calcularTotalDespesasPeriodo(inicio, fim);
    }

    @GetMapping("/por-categoria/{categoriaId}")
    public List<DespesasResponseDTO> listarDespesasPorCategoria(@PathVariable Long categoriaId) {
        return despesaService.listarPorCategoria(categoriaId);
    }

    @GetMapping("/por-categoria-periodo")
    public List<DespesasResponseDTO> listarDespesasPorCategoriaEPeriodo(
            @RequestParam Long categoriaId,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {
        return despesaService.listarPorCategoriaEPeriodo(categoriaId, inicio, fim);
    }

    @GetMapping("/total-por-categoria")
    public BigDecimal calcularTotalDespesasPorCategoria(
            @RequestParam Long categoriaId,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {
        return despesaService.calcularTotalPorCategoria(categoriaId, inicio, fim);
    }

    @PatchMapping("/{id}/parcial")
    public ResponseEntity<DespesasResponseDTO> atualizarDespesasParcialmente(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(despesaService.atualizarParcialmente(id, updates));

    }
}
