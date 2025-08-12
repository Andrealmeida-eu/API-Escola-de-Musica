package com.alangodoy.studioApp.s.business.converter.receita;

import com.alangodoy.studioApp.s.business.dto.out.receita.CategoriaDespesasResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.CategoriaDespesas;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CategoriaDespesasConverter {

    public CategoriaDespesasResponseDTO toDTO(CategoriaDespesas categoriaDespesas) {
        CategoriaDespesasResponseDTO dto = new CategoriaDespesasResponseDTO();
        dto.setNome(categoriaDespesas.getNome());
        dto.setDescricao(categoriaDespesas.getDescricao());

        return dto;
    }

}
