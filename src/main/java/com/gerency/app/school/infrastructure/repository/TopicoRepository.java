package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    // Query JPQL para buscar tópicos por disciplina
    @Query("SELECT t FROM Topico t WHERE t.disciplina.id = :disciplinaId")
    List<Topico> findByDisciplinaId(Long disciplinaId);


    @Modifying
    @Query(value = "DELETE FROM topicos WHERE disciplina_id = :disciplinaId", nativeQuery = true)
    void deleteByDisciplinaIdInTransaction(@Param("disciplinaId") Long disciplinaId);

    @Modifying
    @Query("DELETE FROM Topico t WHERE t.disciplina.id = :disciplinaId")
    void deleteByDisciplinaId(@Param("disciplinaId") Long disciplinaId);

    // Busca um tópico por ID e disciplina (validação)
    // Query nativa para validar se um tópico pertence a uma disciplina
    @Query(value = "SELECT * FROM topico WHERE id = :topicoId AND disciplina_id = :disciplinaId", nativeQuery = true)
    Optional<Topico> findByIdAndDisciplinaId(Long topicoId, Long disciplinaId);
}
