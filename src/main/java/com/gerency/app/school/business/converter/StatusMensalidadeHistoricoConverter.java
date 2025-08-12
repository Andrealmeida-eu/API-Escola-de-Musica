package com.alangodoy.studioApp.s.business.converter;

import com.alangodoy.studioApp.s.business.dto.out.StatusMensalidadeHistoricoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.MensalidadeResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Mensalidade;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.StatusMensalidadeHistorico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatusMensalidadeHistoricoConverter {


    public StatusMensalidadeHistoricoResponseDTO toDTO(StatusMensalidadeHistorico historico) {
        return StatusMensalidadeHistoricoResponseDTO.builder()
                .statusAnterior(historico.getStatusAnterior())
                .statusNovo(historico.getStatusNovo())
                .dataModificacao(historico.getDataModificacao())
                .mensalidade(toMensalidadeResponseDTO(historico.getMensalidade()))
                .build();
    }

    public List<StatusMensalidadeHistoricoResponseDTO> toDTOList(List<StatusMensalidadeHistorico> historicos) {
        return historicos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MensalidadeResponseDTO toMensalidadeResponseDTO(Mensalidade entity) {
        MensalidadeResponseDTO dto = new MensalidadeResponseDTO();
        dto.setValor(entity.getValor());
        dto.setDataVencimento(entity.getDataVencimento());
        dto.setDataPagamento(entity.getDataPagamento());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public Mensalidade toMensalidadeRequestEntity(MensalidadeResponseDTO dto) {
        return Mensalidade.builder()
                .valor(dto.getValor())
                .dataVencimento(dto.getDataVencimento())
                .dataPagamento(dto.getDataPagamento())
                .status(dto.getStatus())
                // .tipo(dto.getTipo())
                // .fabricante(dto.getFabricante())
                .build();
    }

}
