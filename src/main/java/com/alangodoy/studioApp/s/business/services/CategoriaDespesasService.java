package com.alangodoy.studioApp.s.business.services;

import com.alangodoy.studioApp.s.business.converter.receita.CategoriaDespesasConverter;
import com.alangodoy.studioApp.s.business.dto.in.receita.CategoriaDespesasRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.receita.CategoriaDespesasResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.EntityUpdate;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.CategoriaDespesas;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ConflitException;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ResourceNotfoundException;
import com.alangodoy.studioApp.s.infrastructure.repository.CategoriaDespesasRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaDespesasService {

    private final CategoriaDespesasRepository categoriaRepository;
    private final CategoriaDespesasConverter categoriaDespesasConverter;


    @Transactional
    public CategoriaDespesasResponseDTO criarCategoriaDespesa(CategoriaDespesasRequestDTO categoriaDTO) {
        CategoriaDespesas categoria = new CategoriaDespesas();
        categoria.setNome(categoriaDTO.getNome());
        categoria.setDescricao(categoriaDTO.getDescricao());
        categoria = categoriaRepository.save(categoria);
        return toDTO(categoria);
    }

    @Transactional
    public CategoriaDespesasResponseDTO atualizarCategoriaDespesa(Long id, CategoriaDespesasRequestDTO categoriaDTO) {
        CategoriaDespesas categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoria.setNome(categoriaDTO.getNome());
        categoria.setDescricao(categoriaDTO.getDescricao());
        categoria = categoriaRepository.save(categoria);
        return toDTO(categoria);
    }

    public List<CategoriaDespesasResponseDTO> listarTodasCategoriaDespesa() {
        return categoriaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDespesasResponseDTO buscarCatDespesaPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    @Transactional
    public CategoriaDespesasResponseDTO atualizarCatDespesaParcialmente(Long id, Map<String, Object> updates) {
        CategoriaDespesas categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Categoria não encontrada"));

        // Validação especial para nome único
        if (updates.containsKey("nome")) {
            String novoNome = (String) updates.get("nome");
            if (!categoria.getNome().equals(novoNome)) {
                if (categoriaRepository.existsByNome(novoNome)) {
                    throw new ConflitException("Já existe uma categoria com este nome");
                }
            }
        }

        try {
            EntityUpdate.updatePartialEntity(categoria, updates);
            return categoriaDespesasConverter.toDTO(categoriaRepository.save(categoria));
        } catch (IllegalAccessException e) {
            throw new ConflitException("Erro ao atualizar categoria: " + e.getMessage());
        }
    }


    @Transactional
    public void deletarCategoriaDespesa(Long id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaDespesasResponseDTO toDTO(CategoriaDespesas categoria) {
        CategoriaDespesasResponseDTO dto = new CategoriaDespesasResponseDTO();
        dto.setNome(categoria.getNome());
        dto.setDescricao(categoria.getDescricao());
        return dto;
    }
}
