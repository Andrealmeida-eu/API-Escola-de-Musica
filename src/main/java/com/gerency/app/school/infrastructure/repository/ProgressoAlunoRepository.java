package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProgressoAlunoRepository extends JpaRepository<ProgressoAluno, Long> {

        @Query("SELECT pa FROM ProgressoAluno pa LEFT JOIN FETCH pa.disciplinas WHERE pa.aluno.id = :alunoId")
        List<ProgressoAluno> findByAlunoIdWithDetails(Long alunoId);

        @Query("SELECT pa FROM ProgressoAluno pa LEFT JOIN FETCH pa.disciplinas WHERE pa.id = :id")
        Optional<ProgressoAluno> findByIdWithDetails(Long id);

        @Query("SELECT pa FROM ProgressoAluno pa LEFT JOIN FETCH pa.disciplinas pd LEFT JOIN FETCH pd.topicos WHERE pa.id = :id")
        Optional<ProgressoAluno> findByIdWithFullDetails(Long id);

     //   @Query("SELECT pa FROM ProgressoAluno pa LEFT JOIN FETCH pa.disciplinas WHERE pa.aluno.id = :alunoId AND pa.principal = true")
//Optional<ProgressoAluno> findByAlunoIdAndPrincipalIsTrue(Long alunoId);
     Optional<ProgressoAluno> findByAlunoId(Long alunoId);


        boolean existsByAlunoIdAndInstrumentoId(Long alunoId, Long instrumentoId);

        @Query("SELECT pa FROM ProgressoAluno pa LEFT JOIN FETCH pa.disciplinas WHERE pa.id = :progressoAlunoId")
        Optional<ProgressoAluno> findByIdWithDisciplinas(Long progressoAlunoId);

        void deleteByAlunoId(Long id);

    }

