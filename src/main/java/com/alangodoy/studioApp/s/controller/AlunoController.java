package com.alangodoy.studioApp.s.controller;


import com.alangodoy.studioApp.s.business.dto.in.aluno.AlunoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.AlunoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoAlunoResponseDTO;
import com.alangodoy.studioApp.s.business.services.AlunoService;
import com.alangodoy.studioApp.s.business.services.ProgressoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController

@RequiredArgsConstructor
@RequestMapping("/admin/escola-musica/alunos")
public class AlunoController {


    private final AlunoService alunoService;
    private final ProgressoService progressoService;


    @PostMapping
    public ResponseEntity<AlunoResponseDTO> cadastrarAluno(@RequestBody @Valid AlunoRequestDTO alunoDTO) {
        AlunoResponseDTO response = alunoService.cadastrarAluno(alunoDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> BuscarAlunoPorId(@PathVariable @Valid Long id) {
        AlunoResponseDTO response = alunoService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/progresso")
    public ResponseEntity<List<ProgressoAlunoResponseDTO>> getProgressao(@PathVariable Long id) {
        List<ProgressoAlunoResponseDTO> progressao = progressoService.buscarTodasProgressoesAluno(id);
        return ResponseEntity.ok(progressao);
    }

    @PatchMapping("/{id}/parcial")
    public ResponseEntity<AlunoResponseDTO> atualizarAlunoParcialmente(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(alunoService.atualizarParcialmente(id, updates));

    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarTodosAlunos() {
        return ResponseEntity.ok(alunoService.listarTodos());
    }

    @DeleteMapping("{id}")
    public void removerAluno(@PathVariable Long id) {
        alunoService.deletarAluno(id);

    }

}
