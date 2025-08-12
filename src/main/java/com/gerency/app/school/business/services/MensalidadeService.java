package com.alangodoy.studioApp.s.business.services;


import com.alangodoy.studioApp.s.business.converter.StatusMensalidadeHistoricoConverter;
import com.alangodoy.studioApp.s.business.converter.aluno.MensalidadeConverter;
import com.alangodoy.studioApp.s.business.dto.out.StatusMensalidadeHistoricoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.ConfigResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.MensalidadeResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.ConfigMensalidade;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Mensalidade;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.StatusMensalidadeHistorico;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusMensalidade;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ResourceNotfoundException;
import com.alangodoy.studioApp.s.infrastructure.repository.AlunoRepository;
import com.alangodoy.studioApp.s.infrastructure.repository.ConfigMensalidadeRepository;
import com.alangodoy.studioApp.s.infrastructure.repository.MensalidadeRepository;
import com.alangodoy.studioApp.s.infrastructure.repository.StatusMensalidadeHistoricoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MensalidadeService {

    private final MensalidadeRepository mensalidadeRepository;
    private final MensalidadeConverter mensalidadeConverter;
    private final AlunoRepository alunoRepository;
    private final StatusMensalidadeHistoricoConverter statusMensalidadeHistoricoConverter;
    private final StatusMensalidadeHistoricoRepository statusMensalidadeHistoricoRepository;
    private final ConfigMensalidadeRepository configuracaoRepository;


    @Transactional
    public void atualizarValorGlobal(BigDecimal novoValor) {
        LocalDate hoje = LocalDate.now();

        List<Mensalidade> futurasNaoPagas = mensalidadeRepository
                .findByDataVencimentoAfterAndDataPagamentoIsNull(hoje);

        futurasNaoPagas.forEach(m -> {
            m.setValor(novoValor);
            m.setDataUltimaAtualizacao(hoje);
        });
    }

    public MensalidadeResponseDTO marcarComoPaga(Long idMensalidade, LocalDate dataPagamento) {
        Mensalidade mensalidade = mensalidadeRepository.findById(idMensalidade)
                .orElseThrow(() -> new EntityNotFoundException("Mensalidade não encontrada"));

        if (mensalidade.getStatus() != StatusMensalidade.PAGA) {

            registrarHistorico(mensalidade);

            mensalidade.setStatus(StatusMensalidade.PAGA);
            mensalidade.setDataPagamento(dataPagamento);

            gerarProximaMensalidade(mensalidade);

            return mensalidadeConverter.toDTO(mensalidadeRepository.save(mensalidade));


        }
        return mensalidadeConverter.toDTO(mensalidade);
    }

    @Scheduled(cron = "0 0 0 10 * ?") // Executa dia 10 após a meia-noite
    public void atualizarMensalidadesAtrasadas() {
        LocalDate hoje = LocalDate.now();

        List<Mensalidade> mensalidadesAbertas = mensalidadeRepository
                .findByStatusAndDataVencimentoBefore(StatusMensalidade.ABERTA, hoje);

        mensalidadesAbertas.forEach(m -> {
            m.setStatus(StatusMensalidade.ATRASADA);
            mensalidadeRepository.save(m);
        });
    }


    public List<StatusMensalidadeHistoricoResponseDTO> buscarHistoricoPorReposicaoIdEPeriodo(
            Long id, LocalDateTime inicio, LocalDateTime fim) {

        Mensalidade mensalidade = mensalidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Reposição não encontrada com ID: " + id));

        return statusMensalidadeHistoricoConverter.toDTOList(statusMensalidadeHistoricoRepository.findByMensalidadeAndDataAlteracaoBetween(mensalidade, inicio, fim));
    }

    public List<StatusMensalidadeHistoricoResponseDTO> buscarHistoricoPorPeriodo(
            Long mensalidadeId, LocalDate inicio, LocalDate fim) {

        Mensalidade mensalidade = mensalidadeRepository.findById(mensalidadeId)
                .orElseThrow(() -> new ResourceNotfoundException("Mensalidade não encontrada"));

        return statusMensalidadeHistoricoRepository.findHistoricoByMensalidadeIdAndPeriodo(mensalidadeId, inicio, fim)
                .stream()
                .map(statusMensalidadeHistoricoConverter::toDTO)
                .collect(Collectors.toList());
    }


    public List<MensalidadeResponseDTO> listarMensalidadesPorAluno(Long alunoId) {
        return mensalidadeConverter.toDTOList(mensalidadeRepository.findByAlunoId(alunoId));
    }


    private StatusMensalidadeHistoricoResponseDTO registrarHistorico(Mensalidade mensalidade) {
        StatusMensalidadeHistorico historico = StatusMensalidadeHistorico.builder()
                .statusAnterior(mensalidade.getStatus())
                .statusNovo(StatusMensalidade.PAGA)
                .dataModificacao(LocalDate.now())
                .mensalidade(mensalidade)
                .motivo("Pagamento registrado")
                .build();

        statusMensalidadeHistoricoRepository.save(historico);
        return mensalidadeConverter.toHistoryDTO(historico);
    }

    private Mensalidade gerarProximaMensalidade(Mensalidade mensalidadePaga) {
        ConfigResponseDTO config = buscarConfiguracao();
        Mensalidade novaMensalidade = new Mensalidade();
        novaMensalidade.setAluno(mensalidadePaga.getAluno());
        novaMensalidade.setValor(getValorMensalidadePadrao()); // ou buscar valor padrão
        novaMensalidade.setDataVencimento(LocalDate.now()
                .plusMonths(1)
                .withDayOfMonth(config.getDiaVencimento()));
        novaMensalidade.setStatus(StatusMensalidade.ABERTA);
        novaMensalidade.setAno(LocalDate.now().getYear());

        return mensalidadeRepository.save(novaMensalidade);
    }


    @Scheduled(cron = "0 0 1 1 * ?") // No primeiro dia de cada mês
    public void gerarMensalidadesMensais() {
        ConfigResponseDTO config = buscarConfiguracao();
        if (config == null) {
            log.error("Configuração de mensalidade não encontrada!");
            return;
        }

        log.info("Iniciando geração de mensalidades...");
        List<Aluno> alunosAtivos = alunoRepository.findByAtivoTrue();
        LocalDate vencimento = LocalDate.now()
                .plusMonths(1)
                .withDayOfMonth(Math.min(config.getDiaVencimento(), LocalDate.now().plusMonths(1).lengthOfMonth()));

        alunosAtivos.forEach(aluno -> {
            try {
                boolean existeMensalidade = mensalidadeRepository.existsByAlunoAndDataVencimentoBetween(
                        aluno,
                        vencimento.withDayOfMonth(1),
                        vencimento.withDayOfMonth(vencimento.lengthOfMonth())
                );

                if (!existeMensalidade) {
                    Mensalidade nova = Mensalidade.builder()
                            .aluno(aluno)
                            .valor(config.getValorMensalidade())
                            .dataVencimento(vencimento)
                            .status(StatusMensalidade.ABERTA)
                            .ano(LocalDate.now().getYear())
                            .build();
                    mensalidadeRepository.save(nova);
                }
            } catch (Exception e) {
                log.error("Erro ao processar aluno ID: {}", aluno.getId(), e);
            }
        });
        log.info("Geração de mensalidades concluída para {} alunos.", alunosAtivos.size());
    }


    // ==================================== METODOS CONFIG_MENSALIDADE =============================================


    @Transactional
    public ConfigResponseDTO criarOuAtualizarConfiguracao(BigDecimal valorMensalidade, Integer diaVencimento) {
        ConfigMensalidade config = configuracaoRepository.findConfiguracao();

        if (config == null) {
            config = new ConfigMensalidade();
        }

        config.setValorMensalidade(valorMensalidade);
        config.setDiaVencimento(diaVencimento);

        return mensalidadeConverter.fromEntity(configuracaoRepository.save(config));
    }

    public ConfigResponseDTO buscarConfiguracao() {
        return mensalidadeConverter.fromEntity(configuracaoRepository.findConfiguracao());
    }

    public BigDecimal getValorMensalidadePadrao() {
        ConfigMensalidade config = configuracaoRepository.findConfiguracao();
        if (config == null) {
            throw new ResourceNotfoundException("Configuração não encontrada");
        }
        return config.getValorMensalidade();
    }


    @Transactional
    public void atualizarDiaVencimento(Integer novoDia) {
        if (novoDia < 1 || novoDia > 31) {
            throw new IllegalArgumentException("Dia de vencimento inválido. Use entre 1 e 31.");
        }

        ConfigMensalidade config = configuracaoRepository.findConfiguracao();
        if (config == null) {
            config = new ConfigMensalidade();
            config.setValorMensalidade(config.getValorMensalidade()); // Valor padrão
        }

        config.setDiaVencimento(novoDia);
        configuracaoRepository.save(config);
    }


}

