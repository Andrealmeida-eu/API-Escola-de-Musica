package com.alangodoy.studioApp.s.controller;

import com.alangodoy.studioApp.s.business.dto.out.aluno.AulaFilterDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.AulaResponseDTO;
import com.alangodoy.studioApp.s.business.services.AulaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/admin/escola-musica/aula")
@RequiredArgsConstructor
public class AulaController {
    private final AulaService aulaService;

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<AulaResponseDTO>> buscarPorAluno(@PathVariable Long alunoId) {
        return ResponseEntity.ok(aulaService.buscarAulasPorAluno(alunoId));
    }

    @GetMapping("/todas")
    public ResponseEntity<List<AulaResponseDTO>> buscarTodasAulas() {
        List<AulaResponseDTO> aulas = aulaService.buscarTodasAulas();
        return ResponseEntity.ok(aulas);
    }

    @GetMapping("/{id}")
    public ResponseEntity <AulaResponseDTO> buscarAulasPorId(@PathVariable Long id) {
        AulaResponseDTO aula = aulaService.buscarAulaPorId(id);
        return ResponseEntity.ok(aula);
    }


    @GetMapping("/proximas/{alunoId}")
    public ResponseEntity<List<AulaResponseDTO>> buscarProximasAulas(
            @PathVariable Long alunoId,
            @RequestParam(defaultValue = "4") int semanas) {

        List<AulaResponseDTO> aulas = aulaService.buscarProximasAulas(alunoId, semanas);
        return ResponseEntity.ok(aulas);
    }

    @GetMapping("/hoje")
    public ResponseEntity<List<AulaResponseDTO>> buscarAulasHoje() {
        return ResponseEntity.ok(aulaService.buscarAulasHoje());
    }

    @GetMapping("/semana")
    public ResponseEntity<List<AulaResponseDTO>> buscarAulasEstaSemana() {
        return ResponseEntity.ok(aulaService.buscarAulasEstaSemana());
    }

    @GetMapping("/mes")
    public ResponseEntity<List<AulaResponseDTO>> buscarAulasEsteMes() {
        return ResponseEntity.ok(aulaService.buscarAulasEsteMes());
    }

    @GetMapping("/dia/{diaSemana}")
    public ResponseEntity<List<AulaResponseDTO>> buscarPorDiaSemana(@PathVariable DayOfWeek diaSemana) {
        return ResponseEntity.ok(aulaService.buscarAulasPorDiaSemana(diaSemana));
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<AulaResponseDTO>> buscarComFiltro(@RequestBody AulaFilterDTO filtro) {
        return ResponseEntity.ok(aulaService.buscarAulasComFiltro(filtro));
    }
}

