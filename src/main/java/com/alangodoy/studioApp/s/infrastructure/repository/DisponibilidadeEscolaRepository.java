package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.DisponibilidadeEscola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadeEscolaRepository extends JpaRepository<DisponibilidadeEscola, Long> {
    List<DisponibilidadeEscola> findAll();

    List<DisponibilidadeEscola> findByAtivoTrue();
}