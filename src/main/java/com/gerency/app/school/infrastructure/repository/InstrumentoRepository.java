package com.alangodoy.studioApp.s.infrastructure.repository;


import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.alangodoy.studioApp.s.infrastructure.enums.InstrumentoTipo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstrumentoRepository extends JpaRepository<Instrumento, Long> {

   // Consultas básicas
   boolean existsByNome(String nome);
   boolean existsByNomeIgnoreCase(String nome);

   // Consulta otimizada para professores
   @EntityGraph(attributePaths = {"professores"})
   Optional<Instrumento> findByNome(String nome);

   // Consulta para alunos
   @EntityGraph(attributePaths = {"alunos"})
   List<Instrumento> findByTipo(InstrumentoTipo tipo);

   // Consulta com filtros avançados
   @Query("SELECT i FROM Instrumento i WHERE " +
           "i.quantidadeDeAluno < :limite AND " +
           "i.tipo = :tipo")
   List<Instrumento> findDisponiveisPorTipo(
           @Param("tipo") InstrumentoTipo tipo,
           @Param("limite") Integer limite);

   // Atualização otimizada
   @Modifying
   @Query("UPDATE Instrumento i SET i.quantidadeDeAluno = i.quantidadeDeAluno + 1 WHERE i.id = :id")
   void incrementarQuantidadeAlunos(@Param("id") Long id);

   // --- NOVAS CONSULTAS REFATORADAS ---

   // Busca instrumento com conteúdo completo (usando EntityGraph)
   @EntityGraph(attributePaths = {
           "conteudoProgramatico",
           "conteudoProgramatico.disciplinas",
           "conteudoProgramatico.disciplinas.topicos"
   })
   @Query("SELECT DISTINCT i FROM Instrumento i WHERE i.id = :id")
   Optional<Instrumento> findWithConteudoCompletoById(Long id);

   // Busca instrumento com conteúdo programático básico
   @EntityGraph(attributePaths = {"conteudoProgramatico"})
   Optional<Instrumento> findWithConteudoById(Long id);

   /*Busca todos os instrumentos com conteúdo (para listagens)
   @EntityGraph(attributePaths = {"conteudoProgramatico"})
   List<Instrumento> findAllWithConteudo();
   
    */



}