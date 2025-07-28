package com.alangodoy.studioApp.s.infrastructure.repository;


import com.alangodoy.studioApp.s.infrastructure.entity.Reposicao;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusReposicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReposicaoRepository extends JpaRepository<Reposicao, Long> {

    // Listar reposições de um dia específico
    @Query("SELECT r FROM Reposicao r WHERE DATE(r.aulaReposicao.dataHora) = :data")
    List<Reposicao> findByData(LocalDate data);

    // Listar reposições de um mês específico
    @Query("SELECT r FROM Reposicao r WHERE YEAR(r.aulaReposicao.dataHora) = :ano AND MONTH(r.aulaReposicao.dataHora) = :mes")
    List<Reposicao> findByMes(int ano, int mes);

    // Listar reposições futuras a partir de hoje
    @Query("SELECT r FROM Reposicao r WHERE r.aulaReposicao.dataHora >= :dataAtual ORDER BY r.aulaReposicao.dataHora ASC")
    List<Reposicao> findProximasReposicoes(LocalDateTime dataAtual);

    // Busca reposições agendadas com data passada
    List<Reposicao> findByStatusAndNovaDataHoraBefore(StatusReposicao status, LocalDateTime dataHora);

    // Busca reposições por aluno
    @Query("SELECT r FROM Reposicao r WHERE r.aulaOriginal.aluno.id = :alunoId")
    List<Reposicao> findByAlunoId(Long alunoId);

    // Busca reposições por professor
    @Query("SELECT r FROM Reposicao r WHERE r.aulaOriginal.professor.id = :professorId")
    List<Reposicao> findByProfessorId(Long professorId);

    // Busca reposições realizadas em um período
    @Query("SELECT r FROM Reposicao r WHERE r.status = 'REALIZADA' AND r.dataRealizacao BETWEEN :inicio AND :fim")
    List<Reposicao> findRealizadasPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT r FROM Reposicao r WHERE r.aulaOriginal.aluno.id = :alunoId")
    List<Reposicao> findByAulaOriginalAlunoId(Long alunoId);

    @Query("SELECT r FROM Reposicao r WHERE r.aulaOriginal.professor.id = :professorId")
    List<Reposicao> findByAulaOriginalProfessorId(Long professorId);

    List<Reposicao> findByStatusAndDataRealizacaoBetween(
            StatusReposicao status,
            LocalDateTime inicio,
            LocalDateTime fim);

}