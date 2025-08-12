package com.alangodoy.studioApp.s.infrastructure.repository;



import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository <Aluno, Long> {

Optional<Aluno> findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    @Query("SELECT a FROM Aluno a WHERE SIZE(a.aulas) > 0")
    List<Aluno> findAlunosComAulas();

    @Query("SELECT DISTINCT a FROM Aluno a " +
            "JOIN FETCH a.aulas au " +
            "WHERE au.dataHora BETWEEN :inicio AND :fim")
    List<Aluno> findAlunosComAulasNoPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    // Consulta otimizada para alunos ativos
    @EntityGraph(attributePaths = {"aulas", "mensalidades"})
    List<Aluno> findByAtivoTrue();

    // Consulta com paginação
    @EntityGraph(attributePaths = {"instrumento"})
    Page<Aluno> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"instrumento", "instrumento.disciplinas"})
    Optional<Aluno> findWithInstrumentoById(Long id);

    boolean existsByNome(String nome);

    List<Aluno> findByAulasRecorrenteTrue();
}
