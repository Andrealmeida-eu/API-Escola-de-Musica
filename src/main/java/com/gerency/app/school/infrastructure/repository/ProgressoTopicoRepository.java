package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoTopico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressoTopicoRepository extends JpaRepository<ProgressoTopico, Long> {

    @EntityGraph(attributePaths = {"topico"})
    List<ProgressoTopico> findByProgressoDisciplinaId(Long progressoDisciplinaId);

    @Query("SELECT pt FROM ProgressoTopico pt WHERE pt.progressoDisciplina.progressoAluno.aluno.id = :alunoId AND pt.status = 'TOPICO_CONCLUIDO'")
    List<ProgressoTopico> findConcluidosByAlunoId(Long alunoId);

    @Query("SELECT pt FROM ProgressoTopico pt WHERE pt.progressoDisciplina.progressoAluno.aluno.id = :alunoId AND pt.concluido = false")
    List<ProgressoTopico> findProximosByAlunoId(Long alunoId);

    // Versão com @Query explícita
    @Query("SELECT pt FROM ProgressoTopico pt WHERE pt.progressoDisciplina.aluno.id = :alunoId")
    List<ProgressoTopico> findByProgressoDisciplina_AlunoId(Long alunoId);
    @Modifying
    @Query(value = "DELETE FROM progresso_topico WHERE topico_id = :topicoId", nativeQuery = true)
    void deleteByTopicoIdInTransaction(@Param("topicoId") Long topicoId);


    // Exclui todos os progressos de um tópico
    @Modifying
    @Query("DELETE FROM ProgressoTopico pt WHERE pt.topico.id = :topicoId")
    void deleteByTopicoId(@Param("topicoId") Long topicoId);


    // Query JPQL para listar todos os tópicos de um aluno
    @Query("SELECT pt FROM ProgressoTopico pt WHERE  pt.progressoDisciplina.id = :progressoDisciplinaId AND pt.topico.id = :topicoId")
    Optional<ProgressoTopico> findByProgressoDisciplinaIdAndTopicoId(Long progressoDisciplinaId, Long topicoId);

    @Query("SELECT pt FROM ProgressoTopico pt JOIN FETCH pt.topico WHERE pt.id = :id")
    Optional<ProgressoTopico> findByIdComTopico(@Param("id") Long id);

}

