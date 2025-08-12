package com.alangodoy.studioApp.s.controller;

import com.alangodoy.studioApp.s.business.dto.in.receita.CategoriaDespesasRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.receita.CategoriaDespesasResponseDTO;
import com.alangodoy.studioApp.s.business.services.CategoriaDespesasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/escola-musica/categoria")
@RequiredArgsConstructor
public class CategoriaDespesaController {

    private final CategoriaDespesasService categoriaService;


    @PostMapping
    public CategoriaDespesasResponseDTO criarCatDes(@RequestBody CategoriaDespesasRequestDTO categoriaDTO) {
        return categoriaService.criarCategoriaDespesa(categoriaDTO);
    }

    @PutMapping("/{id}")
    public CategoriaDespesasResponseDTO atualizarCategoriaDespesa(@PathVariable Long id, @RequestBody CategoriaDespesasRequestDTO categoriaDTO) {
        return categoriaService.atualizarCategoriaDespesa(id, categoriaDTO);
    }

    @GetMapping
    public List<CategoriaDespesasResponseDTO> listarTodasCatDespesas() {
        return categoriaService.listarTodasCategoriaDespesa();
    }

    @GetMapping("/{id}")
    public CategoriaDespesasResponseDTO buscarCatDespesaPorId(@PathVariable Long id) {
        return categoriaService.buscarCatDespesaPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletarCatDespesa(@PathVariable Long id) {
        categoriaService.deletarCategoriaDespesa(id);
    }

    @PatchMapping("/{id}/parcial")
    public ResponseEntity<CategoriaDespesasResponseDTO> atualizarParcialmenteCatDespesa(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(categoriaService.atualizarCatDespesaParcialmente(id, updates));

    }
}
