package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Mensalidade;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusMensalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long> {



    // Busca todas as mensalidades (para atualização em massa)
    List<Mensalidade> findAll();

    List<Mensalidade> findByAlunoId(Long alunoId);


    // Método para buscar mensalidades por intervalo de data de vencimento
    List<Mensalidade> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim);


    List<Mensalidade> findByStatusAndDataVencimentoBefore(
            StatusMensalidade status,
            LocalDate data);


    @Query("SELECT COUNT(m) FROM Mensalidade m WHERE m.status = 'PAGA' AND m.dataPagamento BETWEEN :inicio AND :fim")
    int countMensalidadesPagasNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);


    @Query("SELECT COUNT(m) FROM Mensalidade m WHERE m.status IN ('ABERTA', 'ATRASADA') AND m.dataPagamento BETWEEN :inicio AND :fim")
    int countMensalidadesPendentesNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    List<Mensalidade> findByDataVencimentoAfter(LocalDate data);

    List<Mensalidade> findByDataVencimentoAfterAndDataPagamentoIsNull(LocalDate dataVencimento);

    // Soma o valor das mensalidades PAGAS no período (por data de vencimento)
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Mensalidade m WHERE m.status = :status AND m.dataPagamento BETWEEN :inicio AND :fim")
    BigDecimal sumValorPagoByPeriodo(@Param("status") StatusMensalidade status,
                                     @Param("inicio") LocalDate inicio,
                                     @Param("fim") LocalDate fim);



    // Conta mensalidades PAGAS no período
    long countByStatusAndDataPagamentoBetween(StatusMensalidade status, LocalDate inicio, LocalDate fim);


    boolean existsByAlunoAndDataVencimentoBetween(Aluno aluno, LocalDate localDate, LocalDate localDate1);
}
