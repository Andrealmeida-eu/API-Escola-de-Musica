package com.alangodoy.studioApp.s.business.services;

import com.alangodoy.studioApp.s.business.converter.receita.DespesasConverter;
import com.alangodoy.studioApp.s.business.dto.in.receita.DespesasRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.receita.DespesasResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.EntityUpdate;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.CategoriaDespesas;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Despesas;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ConflitException;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ResourceNotfoundException;
import com.alangodoy.studioApp.s.infrastructure.repository.CategoriaDespesasRepository;
import com.alangodoy.studioApp.s.infrastructure.repository.DespesasRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DespesaService {

    private final DespesasRepository despesaRepository;
    private final CategoriaDespesasRepository categoriaRepository;
    private final DespesasConverter despesasConverter;


        @Transactional
        public DespesasResponseDTO criarDespesa(DespesasRequestDTO despesaDTO) {
            Despesas despesa = new Despesas();
            despesa.setDescricao(despesaDTO.getDescricao());
            despesa.setValor(despesaDTO.getValor());
            despesa.setData(despesaDTO.getData());

            CategoriaDespesas categoria = categoriaRepository.findById(despesaDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            despesa.setCategoria(categoria);

            despesa = despesaRepository.save(despesa);
            return despesasConverter.toDTO(despesa);
        }

        public List<DespesasResponseDTO> listarTodas() {
            return despesaRepository.findAll().stream()
                    .map(despesasConverter::toDTO)
                    .collect(Collectors.toList());
        }

        public List<DespesasResponseDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
            return despesaRepository.findByDataBetween(inicio, fim).stream()
                    .map(despesasConverter::toDTO)
                    .collect(Collectors.toList());
        }

        public List<DespesasResponseDTO> listarPorCategoria(Long categoriaId) {
            return despesaRepository.findByCategoriaId(categoriaId).stream()
                    .map(despesasConverter::toDTO)
                    .collect(Collectors.toList());
        }

        public List<DespesasResponseDTO> listarPorCategoriaEPeriodo(Long categoriaId, LocalDate inicio, LocalDate fim) {
            return despesaRepository.findByCategoriaAndPeriodo(categoriaId, inicio, fim).stream()
                    .map(despesasConverter::toDTO)
                    .collect(Collectors.toList());
        }

        public BigDecimal calcularTotalDespesasPeriodo(LocalDate inicio, LocalDate fim) {
            BigDecimal total = despesaRepository.sumValorByPeriodo(inicio, fim);
            return total != null ? total : BigDecimal.ZERO;
        }

        public BigDecimal calcularTotalPorCategoria(Long categoriaId, LocalDate inicio, LocalDate fim) {
            BigDecimal total = despesaRepository.sumValorByCategoriaAndPeriodo(categoriaId, inicio, fim);
            return total != null ? total : BigDecimal.ZERO;
        }

    @Transactional
    public DespesasResponseDTO atualizarParcialmente(Long id, Map<String, Object> updates) {
        Despesas despesas = despesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Categoria não encontrada"));

        try {
            EntityUpdate.updatePartialEntity(despesas, updates);
            return despesasConverter.toDTO(despesaRepository.save(despesas));
        } catch (IllegalAccessException e) {
            throw new ConflitException("Erro ao atualizar categoria: " + e.getMessage());
        }
    }


    }



