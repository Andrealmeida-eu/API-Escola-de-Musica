package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

    public interface ProfessorRepository extends JpaRepository<Professor, Long> {

        List<Professor> findByInstrumentosId(Long instrumentoId);
        boolean existsByCpf(String cpf);

        boolean existsByNome(String nome);

        @EntityGraph(attributePaths = {"instrumento"})
        Page<Professor> findAll(Pageable pageable);


        // Busca professores que ensinam um instrumento específico
        @Query("SELECT p FROM Professor p JOIN p.instrumentos i WHERE i.id = :instrumentoId")
        List<Professor> findByInstrumentoId(@Param("instrumentoId") Long instrumentoId);

        // Busca professores que não têm instrumentos específicos (ensinam todos)
        @Query("SELECT p FROM Professor p WHERE p.instrumentos IS EMPTY")
        List<Professor> findProfessoresGenericos();

        // Busca professores que ensinam um instrumento específico OU que ensinam todos (método combinado)
        @Query("SELECT DISTINCT p FROM Professor p LEFT JOIN p.instrumentos i " +
                "WHERE i.id = :instrumentoId OR p.instrumentos IS EMPTY")
        List<Professor> findProfessoresDisponiveisParaInstrumento(@Param("instrumentoId") Long instrumentoId);

      
    }
