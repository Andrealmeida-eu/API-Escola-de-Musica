package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.HistoricoStatusReposicao;
import com.alangodoy.studioApp.s.infrastructure.entity.Reposicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricoStatusReposicaoRepository extends JpaRepository<HistoricoStatusReposicao, Long> {

    List<HistoricoStatusReposicao> findByReposicaoIdOrderByDataAlteracaoDesc(Long reposicaoId);


    @Query("SELECT h FROM HistoricoStatusReposicao h WHERE h.reposicao.aulaOriginal.aluno.id = :alunoId")
    List<HistoricoStatusReposicao> findByAlunoId(Long alunoId);



    List<HistoricoStatusReposicao> findByReposicaoAndDataAlteracaoBetween(
            Reposicao reposicao,
            LocalDateTime inicio,
            LocalDateTime fim);
}
