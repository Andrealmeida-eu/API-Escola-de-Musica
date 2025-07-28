package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoDisciplina;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressoDisciplinaRepository extends JpaRepository<ProgressoDisciplina, Long> {

   @Query("SELECT pd FROM ProgressoDisciplina pd LEFT JOIN FETCH pd.topicos WHERE pd.id = :id")
   Optional<ProgressoDisciplina> findByIdWithTopicos(Long id);

      @EntityGraph(attributePaths = {"topicos", "disciplina"})
      @Query("SELECT pd FROM ProgressoDisciplina pd WHERE pd.progressoAluno.id = :progressoAlunoId")
      List<ProgressoDisciplina> findByProgressoAlunoIdWithTopicos(Long progressoAlunoId);

   // Busca progresso de um aluno em uma disciplina específica    // Query JPQL para buscar progresso de um aluno em uma disciplina
   Optional<ProgressoDisciplina> findByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId);

}
