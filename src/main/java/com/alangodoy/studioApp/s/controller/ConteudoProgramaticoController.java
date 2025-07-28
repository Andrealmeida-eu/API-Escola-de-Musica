package com.alangodoy.studioApp.s.controller;


import com.alangodoy.studioApp.s.business.dto.in.conteudo.ConteudoProgramaticoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.in.conteudo.DisciplinaRequestDTO;
import com.alangodoy.studioApp.s.business.dto.in.conteudo.TopicoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.ConteudoProgramaticoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.DisciplinaResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.TopicoResponseDTO;
import com.alangodoy.studioApp.s.business.services.ConteudoProgramaticoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/escola-musica/conteudo-programatico")
@RequiredArgsConstructor
public class ConteudoProgramaticoController {

 private final ConteudoProgramaticoService conteudoService;

    // ========== ENDPOINTS PRINCIPAIS ========== //

    @PostMapping
    public ResponseEntity<ConteudoProgramaticoResponseDTO> criarConteudoCompleto(
            @RequestBody @Valid ConteudoProgramaticoRequestDTO dto) {
        ConteudoProgramaticoResponseDTO response = conteudoService.criarConteudoCompleto(dto.getInstrumentoId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{instrumentoId}")
    public ResponseEntity<ConteudoProgramaticoResponseDTO> atualizarConteudoCompleto(
            @PathVariable Long instrumentoId,
            @RequestBody @Valid ConteudoProgramaticoRequestDTO dto) {
        ConteudoProgramaticoResponseDTO response = conteudoService.atualizarConteudoCompleto(instrumentoId, dto);
        return ResponseEntity.ok(response);
    }

    // ========== ENDPOINTS DE CONSULTA ========== //

    @GetMapping("/instrumento/{instrumentoId}")
    public ResponseEntity<ConteudoProgramaticoResponseDTO> buscarConteudoCompleto(
            @PathVariable Long instrumentoId) {
        ConteudoProgramaticoResponseDTO response = conteudoService.buscarConteudoCompleto(instrumentoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ConteudoProgramaticoResponseDTO>> listarTodosConteudos() {
        List<ConteudoProgramaticoResponseDTO> response = conteudoService.listarTodosConteudos();
        return ResponseEntity.ok(response);
    }

    // ========== ENDPOINTS DE DISCIPLINAS ========== //

    @PostMapping("/disciplinas")
    public ResponseEntity<DisciplinaResponseDTO> adicionarDisciplina(
            @RequestBody @Valid DisciplinaRequestDTO dto) {
        DisciplinaResponseDTO response = conteudoService.adicionarDisciplina(dto.getConteudoId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/disciplinas/{disciplinaId}")
    public ResponseEntity<DisciplinaResponseDTO> atualizarDisciplina(
            @PathVariable Long disciplinaId,
            @RequestBody @Valid DisciplinaRequestDTO dto) {
        DisciplinaResponseDTO response = conteudoService.atualizarDisciplina(disciplinaId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/disciplinas/{id}")
    public ResponseEntity<Void> excluirDisciplina(@PathVariable Long id) {
        conteudoService.inativarDisciplina(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disciplinas")
    public ResponseEntity<List<DisciplinaResponseDTO>> listarDisciplinas() {
        List<DisciplinaResponseDTO> disciplinas = conteudoService.listarTodas();
        return ResponseEntity.ok(disciplinas);
    }

    // ========== ENDPOINTS DE TÓPICOS ========== //

    @PostMapping("/topicos")
    public ResponseEntity<TopicoResponseDTO> adicionarTopico(
            @RequestBody @Valid TopicoRequestDTO dto) {
        TopicoResponseDTO response = conteudoService.adicionarTopico(dto.getDisciplinaId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/topicos/{topicoId}")
    public ResponseEntity<TopicoResponseDTO> atualizarTopico(
            @PathVariable Long topicoId,
            @RequestBody @Valid TopicoRequestDTO dto) {
        TopicoResponseDTO response = conteudoService.atualizarTopico(topicoId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/topicos/{id}")
    public ResponseEntity<Void> excluirTopico(@PathVariable Long id) {
        conteudoService.inativarTopico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disciplinas/{disciplinaId}/topicos")
    public ResponseEntity<List<TopicoResponseDTO>> listarTopicosPorDisciplina(
            @PathVariable Long disciplinaId) {
        List<TopicoResponseDTO> topicos = conteudoService.listarPorDisciplina(disciplinaId);
        return ResponseEntity.ok(topicos);
    }
    // ========== ENDPOINTS DE REMOÇÃO ========== //

    @DeleteMapping("/{instrumentoId}")
    public ResponseEntity<Void> removerConteudoProgramatico(
            @PathVariable Long instrumentoId) {
        conteudoService.inativarConteudo(instrumentoId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/parcial")
    public ResponseEntity<ConteudoProgramaticoResponseDTO> atualizarParcialmenteConteudoProg(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(conteudoService.atualizarParcialmente(id, updates));

    }
}