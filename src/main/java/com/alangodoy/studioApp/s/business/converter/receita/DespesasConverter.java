package com.alangodoy.studioApp.s.business.converter.receita;

import com.alangodoy.studioApp.s.business.dto.out.receita.DespesasResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Despesas;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class DespesasConverter {

    public DespesasResponseDTO toDTO(Despesas despesa) {
        DespesasResponseDTO dto = new DespesasResponseDTO();
        dto.setDescricao(despesa.getDescricao());
        dto.setValor(despesa.getValor());
        dto.setData(despesa.getData());
        dto.setCategoriaNome(despesa.getCategoria().getNome()); // Adicione este campo no DTO
        return dto;
    }
}
