package com.alangodoy.studioApp.s.controller;


import com.alangodoy.studioApp.s.business.converter.instrumento.InstrumentoConverter;
import com.alangodoy.studioApp.s.business.dto.in.instrumento.InstrumentoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.instrumento.InstrumentoResponseDTO;
import com.alangodoy.studioApp.s.business.services.InstrumentoService;
import com.alangodoy.studioApp.s.infrastructure.enums.InstrumentoTipo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/escola-musica/instrumentos")
public class InstrumentoController {

    private final InstrumentoService instrumentoService;
    private final InstrumentoConverter instrumentoConverter;


    @PostMapping
    public ResponseEntity<InstrumentoResponseDTO> cadastrarIsntrumentos(
            @RequestBody InstrumentoRequestDTO instrumentoDTO) {
        InstrumentoResponseDTO response = instrumentoService.cadastrarInstrumento(instrumentoDTO);
        return ResponseEntity
                .created(URI.create("/instrumentos/" + instrumentoDTO.getId()))
                .body(response);
    }
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<InstrumentoResponseDTO>> listarIsntrumentosPorCategoria(
            @PathVariable InstrumentoTipo categoria) {
        return ResponseEntity.ok(instrumentoService.listarPorTipo(categoria));
    }

    @GetMapping
    public ResponseEntity<List<InstrumentoResponseDTO>> listarTodosInstrumentos() {
        return ResponseEntity.ok(instrumentoService.listarTodos());

    }

    @PatchMapping("/{id}/parcial")
    public ResponseEntity<InstrumentoResponseDTO> atualizarIsntrumentosParcialmente(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(instrumentoService.atualizarParcialmente(id, updates));

    }



}
