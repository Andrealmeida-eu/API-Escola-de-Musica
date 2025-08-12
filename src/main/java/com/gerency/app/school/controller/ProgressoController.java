package com.alangodoy.studioApp.s.controller;

import com.alangodoy.studioApp.s.business.dto.in.progresso.ProgressoTopicoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoDetalhadoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoResumoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoTopicoResponseDTO;
import com.alangodoy.studioApp.s.business.services.ProgressoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/escola-musica/progresso")
public class ProgressoController {


    private  final ProgressoService progressoService;

    // Endpoint para concluir um tópico
    @PatchMapping("/topicos/{topicoId}/concluir")
    public ResponseEntity<ProgressoTopicoResponseDTO> concluirnTopico(
            @PathVariable Long topicoId
    ) {
        ProgressoTopicoResponseDTO progresso = progressoService.concluirTopico(topicoId);
        return ResponseEntity.ok(progresso);
    }

    @PatchMapping("/alunos/{alunoId}/topicos/{topicoId}/iniciar")
    public ResponseEntity<ProgressoTopicoResponseDTO> iniciarTopico(
            @PathVariable Long alunoId,
            @PathVariable Long topicoId
    ) {
        ProgressoTopicoResponseDTO response = progressoService.iniciarTopico(alunoId, topicoId);
        return ResponseEntity.ok(response);
    }

        @PostMapping
        public ResponseEntity<Void> atualizarProgresso(
                @RequestBody @Valid ProgressoTopicoRequestDTO progressoDTO) {
            progressoService.atualizarProgresso(progressoDTO.getStatus());
            return ResponseEntity.ok().build();
        }


    @GetMapping("/aluno/{alunoId}/resumo")
    public ResponseEntity<ProgressoResumoResponseDTO> getResumoAluno(
            @PathVariable Long alunoId) {

        return ResponseEntity.ok(
                progressoService.obterResumoProgresso(alunoId)
        );
    }

    @GetMapping("/aluno/{alunoId}/proximos-topicos")
    public ResponseEntity<List<ProgressoTopicoResponseDTO>> getProximosTopicos(
            @PathVariable Long alunoId) {

        return ResponseEntity.ok(
                progressoService.buscarProximosTopicos(alunoId)
        );
    }

    @GetMapping("/aluno/{alunoId}/proximas-disciplinas")
    public ResponseEntity<List<ProgressoDetalhadoResponseDTO.ProgressoDisciplinaDTO>> getProximasDisciplinas(
            @PathVariable Long alunoId) {

        return ResponseEntity.ok(
                progressoService.buscarProximasDisciplinas(alunoId)
        );
    }

}
