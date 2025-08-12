package com.alangodoy.studioApp.s.controller;

import com.alangodoy.studioApp.s.business.converter.aluno.ProfessorConverter;
import com.alangodoy.studioApp.s.business.dto.in.aluno.ProfessorRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.ProfessorResponseDTO;
import com.alangodoy.studioApp.s.business.services.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/escola-musica/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;
    private final ProfessorConverter professorConverter;


    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> cadastrarProfessor(
            @Valid @RequestBody ProfessorRequestDTO professorDTO) {

        ProfessorResponseDTO professorSalvo = professorService.cadastrarProfessor(professorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(professorSalvo);
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> listarTodosProfessores() {
        return ResponseEntity.ok(professorService.listarTodos());
    }

    @PatchMapping("/categoria/{id}/parcial")
    public ResponseEntity<ProfessorResponseDTO> atualizarProfessorParcialmente(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(professorService.atualizarParcialmente(id, updates));
    }
}



