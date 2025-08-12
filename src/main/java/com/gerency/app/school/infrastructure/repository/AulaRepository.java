package com.alangodoy.studioApp.s.infrastructure.repository;


import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.Aula;
import com.alangodoy.studioApp.s.infrastructure.entity.Professor;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusAula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {

    @EntityGraph(attributePaths = {"aluno", "professor"})
    List<Aula> findByAlunoIdOrderByDataHoraAsc(Long alunoId);

    boolean existsByDiaSemanaAulaAndHorarioAula(DayOfWeek diaSemanaAula, LocalTime horarioAula);

    boolean existsByProfessorIdAndDataHora(Long professorId, LocalDateTime dataHora);

    @EntityGraph(attributePaths = {"aluno"})
    List<Aula> findByAlunoIdAndStatus(Long alunoId, StatusAula status);


    @Query("SELECT a FROM Aula a WHERE FUNCTION('DAYOFWEEK', a.dataHora) = :diaSemana")
    List<Aula> findByDiaDaSemana(@Param("diaSemana") int diaSemana);

    @EntityGraph(attributePaths = {"aluno", "professor"})
    Page<Aula> findAll(Specification<Aula> spec, Pageable pageable);


    // Busca por aluno
    List<Aula> findByAlunoId(Long alunoId);

    // Busca por período
    List<Aula> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    // Busca por aluno e período


    List<Aula> findByAlunoIdAndDataHoraBetween( Long alunoId, LocalDateTime inicio, LocalDateTime fim);

    // Busca por dia da semana
    @Query("SELECT a FROM Aula a WHERE FUNCTION('DAYOFWEEK', a.dataHora) = :diaSemanaValue")
    List<Aula> findByDiaSemana(@Param("diaSemanaValue") int diaSemanaValue);

    // Busca por status
    List<Aula> findByStatus(StatusAula status);

    // Busca completa com Specification
    List<Aula> findAll(Specification<Aula> spec);


    boolean existsByAlunoId(Long alunoId);

    boolean existsByProfessorId(Long professorId);

    boolean existsByProfessorAndDiaSemanaAulaAndHorarioAula(Professor professor, DayOfWeek diaSemana, LocalTime horario);

    List<Aula> findByAlunoAndDataHoraAfter(Aluno aluno, LocalDateTime localDateTime);

    List<Aula> findByAlunoAndRecorrenteTrue(Aluno aluno);


    List<Aula> findByProfessorIdAndDataHoraBetween(Long professorId, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    boolean existsByAlunoAndDataHora(Aluno aluno, LocalDateTime dataHoraAula);

    Optional<Aula> findTopByAlunoOrderByDataHoraDesc(Aluno aluno);
}



