package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Despesas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesasRepository extends JpaRepository<Despesas, Long> {

    // Busca despesas por período
    List<Despesas> findByDataBetween(LocalDate inicio, LocalDate fim);

    // Soma o valor total das despesas em um período
    @Query("SELECT SUM(d.valor) FROM Despesas d WHERE d.data BETWEEN :inicio AND :fim")
    BigDecimal sumValorByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    // Busca despesas por categoria
    List<Despesas> findByCategoriaId(Long categoriaId);

    // Busca despesas por categoria e período
    @Query("SELECT d FROM Despesas d WHERE d.categoria.id = :categoriaId AND d.data BETWEEN :inicio AND :fim")
    List<Despesas> findByCategoriaAndPeriodo(
            @Param("categoriaId") Long categoriaId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    // Soma o valor por categoria em um período
    @Query("SELECT SUM(d.valor) FROM Despesas d WHERE d.categoria.id = :categoriaId AND d.data BETWEEN :inicio AND :fim")
    BigDecimal sumValorByCategoriaAndPeriodo(
            @Param("categoriaId") Long categoriaId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);
}
