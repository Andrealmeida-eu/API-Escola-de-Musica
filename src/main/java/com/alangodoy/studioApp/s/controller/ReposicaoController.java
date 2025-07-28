package com.alangodoy.studioApp.s.controller;

import com.alangodoy.studioApp.s.business.converter.aluno.ReposicaoConverter;
import com.alangodoy.studioApp.s.business.dto.in.ReposicaoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.HistoricoStatusReposicaoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.HorarioDisponivelDTO;
import com.alangodoy.studioApp.s.business.dto.out.ReposicaoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.AulaResponseDTO;
import com.alangodoy.studioApp.s.business.services.AulaService;
import com.alangodoy.studioApp.s.infrastructure.entity.Reposicao;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusReposicao;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/escola-musica/reposicao")
public class ReposicaoController {

    private final AulaService aulaService;
    private final ReposicaoConverter reposicaoConverter;

    @PostMapping("/marcar")
    public ResponseEntity<ReposicaoResponseDTO> marcarReposicao(@RequestBody ReposicaoRequestDTO reposicaoDTO) {
            ReposicaoResponseDTO ReposicaoDTO = aulaService.marcarReposicao(reposicaoDTO);
            return ResponseEntity.ok(ReposicaoDTO);

    }


    @GetMapping("/all")
    public ResponseEntity<List<ReposicaoResponseDTO>> listarTodas() {
        List<ReposicaoResponseDTO> reposicoes = aulaService.getAllRepositions();
        return ResponseEntity.ok(reposicoes);
    }

    @GetMapping("/dia")
    public ResponseEntity<List<ReposicaoResponseDTO>> listarPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<ReposicaoResponseDTO> reposicoes = aulaService.listarReposicoesDoDia(data);
        return ResponseEntity.ok(reposicoes);
    }

    @GetMapping("/mes")
    public ResponseEntity<List<ReposicaoResponseDTO>> listarPorMes(
            @RequestParam int ano,
            @RequestParam int mes) {
        List<ReposicaoResponseDTO> reposicoes = aulaService.listarReposicoesDoMes(ano, mes);
        return ResponseEntity.ok(reposicoes);
    }

    @GetMapping("/proximas")
    public ResponseEntity<List<ReposicaoResponseDTO>> listarProximas() {
        List<ReposicaoResponseDTO> reposicoes = aulaService.listarReposicoesProximas();
        return ResponseEntity.ok(reposicoes);
    }
/*
    @GetMapping("/{id}/historico")
    public ResponseEntity<List<HistoricoStatusReposicaoDTO>> getHistorico(
            @PathVariable Long id) {

        // 1. Verifica se a reposição existe
        Reposicao reposicao = reposicaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Reposição não encontrada com id: " + id));

        // 2. Busca o histórico ordenado por data decrescente
        List<HistoricoStatusReposicao> historico = historicoRepository
                .findByReposicaoIdOrderByDataAlteracaoDesc(id);

        // 3. Converte para DTO
        List<HistoricoStatusReposicaoDTO> historicoDTO = historico.stream()
                .map(historicoConverter::toDTO)
                .collect(Collectors.toList());

        // 4. Retorna a resposta
        return ResponseEntity.ok(historicoDTO);
    }

 */

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusReposicao novoStatus) {
        aulaService.alterarStatusReposicao(id, novoStatus);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReposicaoResponseDTO> getReposicao(@PathVariable Long id) {
        Reposicao reposicao = aulaService.buscarPorId(id);
        return ResponseEntity.ok(reposicaoConverter.toDTO(reposicao));
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<HistoricoStatusReposicaoResponseDTO>> getHistorico(
            @PathVariable Long id) {
        List<HistoricoStatusReposicaoResponseDTO> historico = aulaService.buscarHistoricoPorReposicaoId(id);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/{id}/historico/periodo")
    public ResponseEntity<List<HistoricoStatusReposicaoResponseDTO>> getHistoricoPorPeriodo(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        List<HistoricoStatusReposicaoResponseDTO> historico = aulaService
                .buscarHistoricoPorReposicaoIdEPeriodo(id, inicio, fim);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<ReposicaoResponseDTO>> getReposicoesPorAluno(
            @PathVariable Long alunoId) {
        List<ReposicaoResponseDTO> reposicoes = aulaService.buscarPorAlunoId(alunoId);
        return ResponseEntity.ok(reposicoes);
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<ReposicaoResponseDTO>> getReposicoesPorProfessor(
            @PathVariable Long professorId) {
        List<ReposicaoResponseDTO> reposicoes = aulaService.buscarPorProfessorId(professorId);
        return ResponseEntity.ok(reposicoes);
    }

    @GetMapping("/realizadas")
    public ResponseEntity<List<ReposicaoResponseDTO>> getReposicoesRealizadasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        List<ReposicaoResponseDTO> reposicoes = aulaService.buscarRealizadasPorPeriodo(inicio, fim);
        return ResponseEntity.ok(reposicoes);
    }

    @GetMapping("/disponibilidade/{professorId}")
    public ResponseEntity<List<HorarioDisponivelDTO>> getHorariosDisponiveis(
            @PathVariable Long professorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        List<HorarioDisponivelDTO> horarios = aulaService.getHorariosDisponiveis(professorId, inicio, fim);
        return ResponseEntity.ok(horarios);
    }

}

