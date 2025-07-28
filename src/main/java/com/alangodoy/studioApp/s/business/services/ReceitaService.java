package com.alangodoy.studioApp.s.business.services;

import com.alangodoy.studioApp.s.business.dto.out.receita.ReceitaResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Mensalidade;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusMensalidade;
import com.alangodoy.studioApp.s.infrastructure.exceptions.DataInvalidaException;
import com.alangodoy.studioApp.s.infrastructure.repository.MensalidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private final MensalidadeRepository mensalidadeRepository;
    private final DespesaService despesaService;

    public ReceitaResponseDTO calcularReceita(LocalDate inicio, LocalDate fim) {
        // Busca mensalidades no período
        List<Mensalidade> mensalidades = mensalidadeRepository.findByDataVencimentoBetween(inicio, fim);

        // Calcula receita total (considerando apenas mensalidades pagas)
        BigDecimal receitaTotal = mensalidadeRepository.sumValorPagoByPeriodo(StatusMensalidade.PAGA, inicio, fim);
        if (receitaTotal == null) {
            receitaTotal = BigDecimal.ZERO;
        }

        // Calcula despesas no período
        BigDecimal custoTotal = despesaService.calcularTotalDespesasPeriodo(inicio, fim);

        // Calcula lucro
        BigDecimal lucroTotal = receitaTotal.subtract(custoTotal);

        // Cria DTO com os resultados
        ReceitaResponseDTO receitaDTO = new ReceitaResponseDTO();
        receitaDTO.setReceitaTotal(receitaTotal.doubleValue());
        receitaDTO.setCustoTotal(custoTotal.doubleValue());
        receitaDTO.setLucroTotal(lucroTotal.doubleValue());
        receitaDTO.setDataInicio(inicio);
        receitaDTO.setDataFim(fim);

        // Adiciona métricas adicionais
        receitaDTO.setTotalMensalidades(mensalidades.size());
        receitaDTO.setMensalidadesPagas(mensalidadeRepository.countMensalidadesPagasNoPeriodo(inicio, fim));
        receitaDTO.setMensalidadesPendentes(mensalidadeRepository.countMensalidadesPendentesNoPeriodo(inicio, fim));

        return receitaDTO;
    }

    public long contarMensalidadesPagas(LocalDate inicio, LocalDate fim) {
        return mensalidadeRepository.countByStatusAndDataPagamentoBetween(
                StatusMensalidade.PAGA, inicio, fim);
    }

    public BigDecimal calcularReceitaPeriodo(LocalDate inicio, LocalDate fim) {
        return mensalidadeRepository.sumValorPagoByPeriodo(StatusMensalidade.PAGA, inicio, fim);
    }

    public long contarMensalidadesAbertas(LocalDate inicio, LocalDate fim) {
        return mensalidadeRepository.countByStatusAndDataPagamentoBetween(
                StatusMensalidade.ABERTA, inicio, fim);
    }


    /**
     * Valida se as datas para consulta são válidas
     *
     * @param inicio Data de início
     * @param fim    Data final
     * @throws DataInvalidaException Se alguma validação falhar
     */
    private void validarDatas(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null) {
            throw new DataInvalidaException("Data de início não pode ser nula");
        }

        if (fim == null) {
            throw new DataInvalidaException("Data final não pode ser nula");
        }

        if (fim.isBefore(inicio)) {
            throw new DataInvalidaException("Data final deve ser posterior à data de início");
        }

        LocalDateTime agora = LocalDateTime.now();
        if (inicio.isAfter(agora)) {
            throw new DataInvalidaException("Data de início não pode ser no futuro");
        }

        if (fim.isAfter(agora)) {
            throw new DataInvalidaException("Data final não pode ser no futuro");
        }
    }



}

