package com.alangodoy.studioApp.s.business.converter.aluno;

import com.alangodoy.studioApp.s.business.dto.out.StatusMensalidadeHistoricoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.ConfigResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.MensalidadeResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.ConfigMensalidade;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Mensalidade;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.StatusMensalidadeHistorico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MensalidadeConverter {


    public Mensalidade toEntity(MensalidadeResponseDTO dto) {
        if (dto == null) return null;

        return Mensalidade.builder()
                .valor(dto.getValor())
                .dataVencimento(dto.getDataVencimento())
                .dataPagamento(dto.getDataPagamento())
                .status(dto.getStatus())
                .build();
    }

    public MensalidadeResponseDTO toDTO(Mensalidade entity) {
        if (entity == null) return null;

        MensalidadeResponseDTO dto = new MensalidadeResponseDTO();
dto.setId(entity.getId());
        dto.setValor(entity.getValor());
        dto.setDataVencimento(entity.getDataVencimento());
        dto.setDataPagamento(entity.getDataPagamento());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public List<MensalidadeResponseDTO> toDTOList(List<Mensalidade> mensalidades) {
        return mensalidades.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public StatusMensalidadeHistoricoResponseDTO toHistoryDTO(StatusMensalidadeHistorico entity) {
        if (entity == null) return null;

        StatusMensalidadeHistoricoResponseDTO dto = new StatusMensalidadeHistoricoResponseDTO();
        dto.setStatusNovo(entity.getStatusNovo());
        dto.setStatusAnterior(entity.getStatusAnterior());
        dto.setDataModificacao(entity.getDataModificacao());

        return dto;
    }

    public ConfigResponseDTO fromEntity(ConfigMensalidade config) {
        if (config == null) return null;

        ConfigResponseDTO dto = new ConfigResponseDTO();
        dto.setValorMensalidade(config.getValorMensalidade());
        dto.setDiaVencimento(config.getDiaVencimento());
        return dto;
    }
}
