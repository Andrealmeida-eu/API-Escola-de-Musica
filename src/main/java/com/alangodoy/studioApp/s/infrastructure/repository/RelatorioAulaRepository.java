package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.RelatorioAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RelatorioAulaRepository extends JpaRepository<RelatorioAula, Long> {

    List<RelatorioAula> findByProfessorId(Long professorId);

    List<RelatorioAula> findByDataAulaBetween(LocalDate inicio, LocalDate fim);

    List<RelatorioAula> findByAlunoId(Long alunoId);

    @Query("SELECT r FROM RelatorioAula r WHERE r.professor.id = :professorId AND r.dataAula BETWEEN :inicio AND :fim")
    List<RelatorioAula> findRelatoriosPorProfessorEPeriodo(
            @Param("professorId") Long professorId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);
}
