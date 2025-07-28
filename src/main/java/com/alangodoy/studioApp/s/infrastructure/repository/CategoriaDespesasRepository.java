package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.CategoriaDespesas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaDespesasRepository extends JpaRepository<CategoriaDespesas, Long> {

    Optional<CategoriaDespesas> findByNome(String nome);

    boolean existsByNome(String nome);
}
