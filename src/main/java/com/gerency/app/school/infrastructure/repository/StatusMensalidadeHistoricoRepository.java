package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Mensalidade;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.StatusMensalidadeHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// CORRETO: StatusMensalidadeHistoricoRepository.java
public interface StatusMensalidadeHistoricoRepository extends JpaRepository<StatusMensalidadeHistorico, Long> {


    /**
     * Busca histórico por reposição e período
     */
    // Opção 1: Usando JPQL explícito
    @Query("SELECT h FROM StatusMensalidadeHistorico h " +
            "WHERE h.mensalidade = :mensalidade " +
            "AND h.dataModificacao BETWEEN :inicio AND :fim " +
            "ORDER BY h.dataModificacao DESC")
    List<StatusMensalidadeHistorico> findByMensalidadeAndDataAlteracaoBetween(
            @Param("mensalidade") Mensalidade mensalidade,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);


    // Versão 1: Usando JPQL explícito
    @Query("SELECT h FROM StatusMensalidadeHistorico h WHERE h.mensalidade.id = :mensalidadeId ORDER BY h.dataModificacao DESC")
    List<StatusMensalidadeHistorico> findHistoricoByMensalidadeId(@Param("mensalidadeId") Long mensalidadeId);

    // Versão 2: Query derivation
    //List<StatusMensalidadeHistorico> findByMensalidadeIdOrderByDataAlteracaoDesc(Long mensalidadeId);

    // Busca por período
    @Query("SELECT h FROM StatusMensalidadeHistorico h WHERE h.mensalidade.id = " +
            ":mensalidadeId AND h.dataModificacao BETWEEN :inicio AND :fim ORDER BY h.dataModificacao DESC")
    List<StatusMensalidadeHistorico> findHistoricoByMensalidadeIdAndPeriodo(
            @Param("mensalidadeId") Long mensalidadeId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);
}



