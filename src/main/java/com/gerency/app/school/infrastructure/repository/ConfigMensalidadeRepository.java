package com.alangodoy.studioApp.s.infrastructure.repository;

import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.ConfigMensalidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ConfigMensalidadeRepository extends JpaRepository<ConfigMensalidade, Long> {
    default ConfigMensalidade findConfiguracao() {
        return findAll().stream().findFirst()
                .orElseGet(() -> {
                    ConfigMensalidade configPadrao = new ConfigMensalidade();
                    configPadrao.setDiaVencimento(10);
                    configPadrao.setValorMensalidade(BigDecimal.valueOf(300.00));// ou outro valor padrão
                    return configPadrao;
                });

    }
}
