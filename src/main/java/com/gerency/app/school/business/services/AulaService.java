package com.alangodoy.studioApp.s.business.services;

import com.alangodoy.studioApp.s.business.converter.aluno.AlunoConverter;
import com.alangodoy.studioApp.s.business.converter.aluno.AulaConverter;
import com.alangodoy.studioApp.s.business.converter.aluno.HistoricoStatusReposicaoConverter;
import com.alangodoy.studioApp.s.business.converter.aluno.ReposicaoConverter;
import com.alangodoy.studioApp.s.business.dto.in.ReposicaoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.HistoricoStatusReposicaoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.HorarioDisponivelDTO;
import com.alangodoy.studioApp.s.business.dto.out.ReposicaoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.AulaFilterDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.AulaResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.Aula;
import com.alangodoy.studioApp.s.infrastructure.entity.DisponibilidadeEscola;
import com.alangodoy.studioApp.s.infrastructure.entity.Reposicao;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusAula;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusReposicao;
import com.alangodoy.studioApp.s.infrastructure.enums.TipoAula;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ResourceNotfoundException;
import com.alangodoy.studioApp.s.infrastructure.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AulaService {

    private final AulaRepository aulaRepository;
    private final AlunoRepository alunoRepository;
    private final AulaConverter aulaConverter;
    private final AlunoConverter alunoConverter;
    private final ReposicaoConverter reposicaoConverter;
    private final ReposicaoRepository reposicaoRepository;
    private final HistoricoStatusReposicaoRepository historicoRepository;
    private final HistoricoStatusReposicaoConverter historicoConverter;
    private final DisponibilidadeEscolaRepository disponibilidadeEscolaRepository;


    public List<AulaResponseDTO> buscarTodasAulas() {
        List<Aula> aulas = aulaRepository.findAll();
        return aulaConverter.toDTOList(aulas);
    }


    public AulaResponseDTO buscarAulaPorId(Long id) {
        Aula aula = aulaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return aulaConverter.toDTO(aula);
    }

    public List<AulaResponseDTO> buscarProximasAulas(Long alunoId, int semanas) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotfoundException("Aluno não encontrado"));

        // Busca aulas existentes e recorrentes
        List<Aula> aulasExistentes = aulaRepository.findByAlunoAndRecorrenteTrue(aluno);

        List<Aula> aulas = new ArrayList<>();

        if (!aulasExistentes.isEmpty()) {
            // Pega o padrão de recorrência da primeira aula recorrente
            Aula aulaRecorrente = aulasExistentes.get(0);
            DayOfWeek diaSemana = aulaRecorrente.getDiaSemanaAula();
            LocalTime horario = aulaRecorrente.getHorarioPadrao();

            LocalDate hoje = LocalDate.now();
            for (int i = 0; i < semanas; i++) {
                LocalDate dataAula = hoje.with(TemporalAdjusters.nextOrSame(diaSemana))
                        .plusWeeks(i);

                LocalDateTime dataHora = LocalDateTime.of(dataAula, horario);

                // Verifica se já existe aula criada para esta data
                Optional<Aula> aulaExistente = aulasExistentes.stream()
                        .filter(a -> a.getDataHora().equals(dataHora))
                        .findFirst();

                if (aulaExistente.isPresent()) {
                    aulas.add(aulaExistente.get());
                } else if (dataHora.isAfter(LocalDateTime.now())) {
                    // Se não existe e é futura, cria uma nova instância (sem persistir)
                    Aula novaAula = new Aula();
                    novaAula.setAluno(aluno);
                    novaAula.setProfessor(aulaRecorrente.getProfessor());
                    novaAula.setDataHora(dataHora);
                    novaAula.setStatus(StatusAula.AGENDADA);
                    novaAula.setTipoAula(TipoAula.AULA_REGULAR);
                    novaAula.setObservacoes("Aula recorrente semanal");
                    novaAula.setRecorrente(true);
                    novaAula.setDiaSemanaAula(diaSemana);
                    novaAula.setHorarioPadrao(horario);
                    novaAula.setHorarioAula(horario);

                    aulas.add(novaAula);
                }
            }
        }

        return aulaConverter.toDTOList(aulas);
    }

    public List<AulaResponseDTO> buscarAulasPorAluno(Long alunoId) {
        return aulaConverter.toDTOList(aulaRepository.findByAlunoId(alunoId));
    }

    public List<AulaResponseDTO> buscarAulasHoje() {
        LocalDate hoje = LocalDate.now();
        List<Aula> aulas = aulaRepository.findByDataHoraBetween(
                hoje.atStartOfDay(),
                hoje.atTime(LocalTime.MAX)
        );

        return validarListaVazia(aulas);
    }

    public List<AulaResponseDTO> buscarAulasEstaSemana() {
        LocalDate hoje = LocalDate.now();
        LocalDate fimDaSemana = hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        List<Aula> aulas = aulaRepository.findByDataHoraBetween(
                hoje.atStartOfDay(),
                fimDaSemana.atTime(LocalTime.MAX));

        return validarListaVazia(aulas);
    }

    public List<AulaResponseDTO> buscarAulasEsteMes() {
        LocalDate hoje = LocalDate.now();
        List<Aula> aulas = aulaRepository.findByDataHoraBetween(
                hoje.withDayOfMonth(1).atStartOfDay(),
                hoje.withDayOfMonth(hoje.lengthOfMonth()).atTime(LocalTime.MAX)
        );

        return validarListaVazia(aulas);
    }

    public List<AulaResponseDTO> buscarAulasPorDiaSemana(DayOfWeek diaSemana) {
        return aulaConverter.toDTOList(aulaRepository.findByDiaSemana(diaSemana.getValue() + 1));
    }

    public List<AulaResponseDTO> buscarAulasComFiltro(AulaFilterDTO filtro) {
        Specification<Aula> spec = AulaSpecification.comFiltro(filtro);
        return aulaConverter.toDTOList(aulaRepository.findAll(spec));
    }


    @Transactional
    public ReposicaoResponseDTO marcarReposicao(ReposicaoRequestDTO reposicaoDTO) {
        // 1. Validação inicial do DTO
        if (reposicaoDTO.getNovaDataHora() == null) {
            throw new IllegalArgumentException("A nova data/hora é obrigatória");
        }

        // 2. Busca a aula original
        Aula aulaOriginal = aulaRepository.findById(reposicaoDTO.getAulaOriginalId())
                .orElseThrow(() -> new RuntimeException("Aula original não encontrada"));

        // 3. Validações de negócio
        if (aulaOriginal.getReposicao() != null) {
            throw new IllegalStateException("Esta aula já possui uma reposição agendada");
        }

        if (reposicaoDTO.getNovaDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data de reposição deve ser futura");
        }


        // 4. Cria e persiste a aula de reposição PRIMEIRO
        Aula aulaReposicao = new Aula();
        // 2. Cria a aula de reposição com todas as relações
        aulaReposicao.setAluno(aulaOriginal.getAluno()); // ESSENCIAL
        aulaReposicao.setProfessor(aulaOriginal.getProfessor()); // ESSENCIAL
        aulaReposicao.setDataHora(reposicaoDTO.getNovaDataHora());
        aulaReposicao.setDuracao(60);
        aulaReposicao.setStatus(StatusAula.AGENDADA);
        aulaReposicao.setTipoAula(TipoAula.AULA_REPOSICAO);
        aulaReposicao.setObservacoes("Reposição da aula de " + aulaOriginal.getDataHora());
        aulaReposicao.setDiaSemanaAula(DayOfWeek.of(reposicaoDTO.getNovaDataHora().getDayOfWeek().getValue()));
        aulaReposicao.setHorarioAula(reposicaoDTO.getNovaDataHora().toLocalTime());
        aulaReposicao = aulaRepository.save(aulaReposicao);

        // 5. Cria a reposição COM TODOS OS CAMPOS OBRIGATÓRIOS
        Reposicao reposicao = new Reposicao();
        reposicao.setAulaOriginal(aulaOriginal);
        reposicao.setAulaReposicao(aulaReposicao);
        reposicao.setNovaDataHora(reposicaoDTO.getNovaDataHora()); // ESSENCIAL
        reposicao.setMotivo(reposicaoDTO.getMotivo());
        reposicao.setStatus(StatusReposicao.AGENDADA);
        reposicao.setTipoAula(TipoAula.AULA_REPOSICAO);
        reposicao.setDataSolicitacao(LocalDateTime.now());

        // 6. Persiste a reposição
        reposicao = reposicaoRepository.save(reposicao);

        // 7. Atualiza a aula original
        aulaOriginal.setStatus(StatusAula.REPOSTA);
        aulaOriginal.setReposicao(reposicao);
        aulaRepository.save(aulaOriginal);

        return aulaConverter.toReposicaoDTO(reposicao);
    }

    public List<ReposicaoResponseDTO> listarReposicoesDoDia(LocalDate data) {
        List<Reposicao> reposicoes = reposicaoRepository.findByData(data);
        return aulaConverter.toReposicaoDTOList(reposicoes);
    }

    public List<ReposicaoResponseDTO> listarReposicoesDoMes(int ano, int mes) {
        List<Reposicao> reposicoes = reposicaoRepository.findByMes(ano, mes);
        return aulaConverter.toReposicaoDTOList(reposicoes);
    }

    public List<ReposicaoResponseDTO> listarReposicoesProximas() {
        List<Reposicao> reposicoes = reposicaoRepository.findProximasReposicoes(LocalDateTime.now());
        return aulaConverter.toReposicaoDTOList(reposicoes);
    }

    @Transactional
    public void alterarStatusReposicao(Long reposicaoId, StatusReposicao novoStatus) {
        Reposicao reposicao = reposicaoRepository.findById(reposicaoId)
                .orElseThrow(() -> new EntityNotFoundException("Reposição não encontrada"));

        StatusReposicao statusAnterior = reposicao.getStatus();
        reposicao.setStatus(novoStatus);
// 2. Busca a aula original
        Aula aulaOriginal = aulaRepository.findById(reposicao.getAulaOriginal().getId())
                .orElseThrow(() -> new RuntimeException("Aula original não encontrada"));

        if (novoStatus == StatusReposicao.CANCELADA) {
            // 7. Atualiza a aula original
            aulaOriginal.setStatus(StatusAula.AGENDADA);
            aulaOriginal.setReposicao(null);
            aulaRepository.save(aulaOriginal);
        }


        reposicaoRepository.save(reposicao);
    }

    @Scheduled(cron = "0 0 2 * * ?") // Diariamente às 2h
    public void atualizarReposicoesRealizadas() {
        LocalDateTime agora = LocalDateTime.now();

        reposicaoRepository.findByStatusAndNovaDataHoraBefore(
                StatusReposicao.AGENDADA,
                agora
        ).forEach(reposicao -> {
            alterarStatusReposicao(reposicao.getId(), StatusReposicao.REALIZADA);
        });
    }

    public List<AulaResponseDTO> listarAulasPorAluno(Long alunoId) {
        return aulaConverter.toDTOList(aulaRepository.findByAlunoId(alunoId));
    }


    public Reposicao buscarPorId(Long id) {
        return reposicaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Reposição não encontrada com ID: " + id));
    }

    public List<HistoricoStatusReposicaoResponseDTO> buscarHistoricoPorReposicaoId(Long id) {
        // Verifica se a reposição existe
        if (!reposicaoRepository.existsById(id)) {
            throw new ResourceNotfoundException("Reposição não encontrada com ID: " + id);
        }

        return historicoConverter.toDTOList(historicoRepository.findByReposicaoIdOrderByDataAlteracaoDesc(id));
    }


    public List<HistoricoStatusReposicaoResponseDTO> buscarHistoricoPorReposicaoIdEPeriodo(
            Long id, LocalDateTime inicio, LocalDateTime fim) {

        Reposicao reposicao = reposicaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Reposição não encontrada com ID: " + id));

        return historicoConverter.toDTOList(historicoRepository.findByReposicaoAndDataAlteracaoBetween(reposicao, inicio, fim));
    }


    public List<ReposicaoResponseDTO> buscarPorAlunoId(Long alunoId) {
        // Verifica se o aluno existe através de uma aula
        if (!aulaRepository.existsByAlunoId(alunoId)) {
            throw new ResourceNotfoundException("Nenhuma aula encontrada para o aluno com ID: " + alunoId);
        }

        return reposicaoConverter.toDTOList(reposicaoRepository.findByAulaOriginalAlunoId(alunoId));
    }


    public List<ReposicaoResponseDTO> buscarPorProfessorId(Long professorId) {
        // Verifica se o professor existe através de uma aula
        if (!aulaRepository.existsByProfessorId(professorId)) {
            throw new ResourceNotfoundException("Nenhuma aula encontrada para o professor com ID: " + professorId);
        }

        return reposicaoConverter.toDTOList(reposicaoRepository.findByAulaOriginalProfessorId(professorId));
    }


    public List<ReposicaoResponseDTO> buscarRealizadasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data de início deve ser anterior à data de fim");
        }

        return reposicaoConverter.toDTOList(reposicaoRepository.findByStatusAndDataRealizacaoBetween(
                StatusReposicao.REALIZADA,
                inicio,
                fim
        ));
    }

    public List<AulaResponseDTO> validarListaVazia(List<Aula> aulas) {
        return aulas.isEmpty()
                ? Collections.emptyList() // Retorna lista vazia
                : aulaConverter.toDTOList(aulas);

    }


    public List<HorarioDisponivelDTO> getHorariosDisponiveis(Long professorId, LocalDate dataInicio, LocalDate dataFim) {
        // 1. Buscar todas as janelas de funcionamento da escola
        List<DisponibilidadeEscola> disponibilidades = disponibilidadeEscolaRepository.findAll();


        // 2. Buscar aulas agendadas no período
        List<Aula> aulasAgendadas = aulaRepository.findByProfessorIdAndDataHoraBetween(
                professorId,
                dataInicio.atStartOfDay(),
                dataFim.atTime(LocalTime.MAX)
        );

        // 3. Gerar slots de 30 minutos respeitando a disponibilidade
        List<HorarioDisponivelDTO> horariosDisponiveis = new ArrayList<>();

        LocalDate dataAtual = dataInicio;
        while (!dataAtual.isAfter(dataFim)) {
            DayOfWeek dia = dataAtual.getDayOfWeek();

            for (DisponibilidadeEscola disp : disponibilidades) {
                if (disp.getDiaSemana() == dia) {
                    LocalDateTime slot = LocalDateTime.of(dataAtual, disp.getHoraInicio());
                    LocalDateTime fim = LocalDateTime.of(dataAtual, disp.getHoraFim());

                    while (slot.isBefore(fim)) {
                        LocalDateTime finalSlot = slot;
                        boolean ocupado = aulasAgendadas.stream()
                                .anyMatch(aula -> aula.getDataHora().equals(finalSlot));

                        if (!ocupado) {
                            horariosDisponiveis.add(HorarioDisponivelDTO.builder().dataHora(slot).build());

                        }

                        slot = slot.plusMinutes(60);
                    }
                }
            }
            dataAtual = dataAtual.plusDays(1);
        }

        return horariosDisponiveis;
    }


    public List<HorarioDisponivelDTO> getAlunoHorariosDisponiveis(Long alunoId, LocalDate dataInicio, LocalDate dataFim) {
        // 1. Buscar todas as janelas de funcionamento da escola
        List<DisponibilidadeEscola> disponibilidades = disponibilidadeEscolaRepository.findByAtivoTrue();


        // 2. Buscar aulas agendadas no período
        List<Aula> aulasAgendadas = aulaRepository.findByAlunoIdAndDataHoraBetween(
                alunoId,
                dataInicio.atStartOfDay(),
                dataFim.atTime(LocalTime.MAX)
        );

        // 3. Gerar slots de 30 minutos respeitando a disponibilidade
        List<HorarioDisponivelDTO> horariosDisponiveis = new ArrayList<>();

        LocalDate dataAtual = dataInicio;
        while (!dataAtual.isAfter(dataFim)) {
            DayOfWeek dia = dataAtual.getDayOfWeek();

            for (DisponibilidadeEscola disp : disponibilidades) {
                if (disp.getDiaSemana() == dia) {
                    LocalDateTime slot = LocalDateTime.of(dataAtual, disp.getHoraInicio());
                    LocalDateTime fim = LocalDateTime.of(dataAtual, disp.getHoraFim());

                    while (slot.isBefore(fim)) {
                        LocalDateTime finalSlot = slot;
                        boolean ocupado = aulasAgendadas.stream()
                                .anyMatch(aula -> aula.getDataHora().equals(finalSlot));

                        if (!ocupado) {
                            horariosDisponiveis.add(HorarioDisponivelDTO.builder().dataHora(slot).build());

                        }

                        slot = slot.plusMinutes(60);
                    }
                }
            }
            dataAtual = dataAtual.plusDays(1);
        }

        return horariosDisponiveis;
    }


    public List<ReposicaoResponseDTO> getAllRepositions() {
        List<Reposicao> reposicao = reposicaoRepository.findAll();

        return reposicaoConverter.toDTOList(reposicao);
    }

}



