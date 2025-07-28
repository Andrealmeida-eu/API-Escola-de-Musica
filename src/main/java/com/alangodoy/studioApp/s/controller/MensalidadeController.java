package com.alangodoy.studioApp.s.controller;

import com.alangodoy.studioApp.s.business.converter.aluno.MensalidadeConverter;
import com.alangodoy.studioApp.s.business.dto.out.StatusMensalidadeHistoricoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.ConfigResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.MensalidadeResponseDTO;
import com.alangodoy.studioApp.s.business.services.MensalidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/escola-musica")
public class MensalidadeController {

    private final MensalidadeService mensalidadeService;
    private final MensalidadeConverter mensalidadeConverter;

    @PostMapping("/mensalidade/atualizar-mensalidades")
    public ResponseEntity<Void> atualizarMensalidades(@RequestParam BigDecimal novoValor) {
        mensalidadeService.atualizarValorGlobal(novoValor);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mensalidade/{alunoId}")
    public ResponseEntity<List<MensalidadeResponseDTO>> listarMensalidadesPorAluno(@PathVariable Long alunoId) {
        List<MensalidadeResponseDTO> mensalidades = mensalidadeService.listarMensalidadesPorAluno(alunoId);
        return ResponseEntity.ok(mensalidades);
    }

    @PutMapping("/mensalidade/{id}/pagar")
    public ResponseEntity<MensalidadeResponseDTO> marcarComoPaga(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPagamento) {

        LocalDate data = dataPagamento != null ? dataPagamento : LocalDate.now();
        return ResponseEntity.ok(mensalidadeService.marcarComoPaga(id, data));
    }

    /**
     * Busca histórico por período
     * GET /api/mensalidades/historico/{mensalidadeId}/periodo?inicio=...&fim=...
     */
    @GetMapping("mensalidade/{mensalidadeId}/periodo")
    public ResponseEntity<List<StatusMensalidadeHistoricoResponseDTO>> getHistoricoPorPeriodo(
            @PathVariable Long mensalidadeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        return ResponseEntity.ok(mensalidadeService.buscarHistoricoPorPeriodo(mensalidadeId, inicio, fim));
    }


    // ==================================== ENDPOINTS CONFIG_MENSALIDADE =============================================

    @PutMapping("/mensalidade/config/dia-vencimento")
    public ResponseEntity<Void> atualizarDiaVencimento(@RequestParam Integer novoDia) {
        mensalidadeService.atualizarDiaVencimento(novoDia);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mensalidade/config/criar")
    public ResponseEntity<ConfigResponseDTO> criarOuAtualizarConfiguracao(
            @RequestBody ConfigResponseDTO request) {
        var config = mensalidadeService.criarOuAtualizarConfiguracao(
                request.getValorMensalidade(),
                request.getDiaVencimento()
        );
        return ResponseEntity.ok(config);
    }

    @GetMapping("/mensalidade/config/listar")
    public ResponseEntity<ConfigResponseDTO> buscarConfiguracao() {
        var config = mensalidadeService.buscarConfiguracao();
        return ResponseEntity.ok(config);
    }
}
