package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
}
