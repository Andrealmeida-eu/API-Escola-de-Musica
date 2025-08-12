package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    // Busca disciplinas ordenadas por nome (para selecionar a primeira)
    @Query(value = "SELECT * FROM disciplina ORDER BY nome ASC", nativeQuery = true)
    List<Disciplina> findAllByOrderByNomeAsc();
    @Query("SELECT d FROM Disciplina d LEFT JOIN FETCH d.topicos WHERE d.id = :id")
    Optional<Disciplina> findByIdWithTopicos(@Param("id") Long id);
    List<Disciplina> findByConteudoId(Long conteudoId);
}
