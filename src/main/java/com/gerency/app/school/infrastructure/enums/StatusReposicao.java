package com.alangodoy.studioApp.s.infrastructure.enums;

public enum StatusReposicao {
    PENDENTE,       // Solicitação feita, aguardando aprovação
    AGENDADA,       // Reposição agendada
    REALIZADA,      // Reposição concluída
    CANCELADA,      // Reposição cancelada
    NAO_REALIZADA   // Reposição não realizada (aluno faltou)
}