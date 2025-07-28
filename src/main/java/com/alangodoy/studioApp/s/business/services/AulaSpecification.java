package com.alangodoy.studioApp.s.business.services;

import com.alangodoy.studioApp.s.business.dto.out.aluno.AulaFilterDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Aula;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class AulaSpecification {

    public static Specification<Aula> comFiltro(AulaFilterDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getAlunoId() != null) {
                predicates.add(cb.equal(root.get("aluno").get("id"), filtro.getAlunoId()));
            }

            if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
                predicates.add(cb.between(root.get("dataHora"),
                        filtro.getDataInicio().atStartOfDay(),
                        filtro.getDataFim().atTime(LocalTime.MAX)));
            }

            if (filtro.getDiaSemana() != null) {
                predicates.add(cb.equal(
                        cb.function("DAYOFWEEK", Integer.class, root.get("dataHora")),
                        filtro.getDiaSemana().getValue() + 1)); // +1 pois DAYOFWEEK no SQL começa em 1 (Domingo)
            }

            if (filtro.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filtro.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}