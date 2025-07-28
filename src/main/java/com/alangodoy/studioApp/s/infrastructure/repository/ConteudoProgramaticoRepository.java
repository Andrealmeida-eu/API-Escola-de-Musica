package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.ConteudoProgramatico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ConteudoProgramaticoRepository.java (complemento)
@Repository
public interface ConteudoProgramaticoRepository extends JpaRepository<ConteudoProgramatico, Long> {

    Optional<ConteudoProgramatico> findByInstrumentoId(Long instrumentoId);

    /**
     * Busca todos os conteúdos programáticos carregando o instrumento associado
     * de forma otimizada (evitando N+1)
     */
    @EntityGraph(attributePaths = {"instrumento"})
    List<ConteudoProgramatico> findAll();


    boolean existsByInstrumentoAndAtivoTrue(Instrumento instrumento);



}