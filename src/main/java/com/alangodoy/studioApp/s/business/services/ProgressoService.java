package com.alangodoy.studioApp.s.business.services;

import com.alangodoy.studioApp.s.business.converter.progresso.ProgressoAlunoResumoConverter;
import com.alangodoy.studioApp.s.business.converter.progresso.ProgressoConverter;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoAlunoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoDetalhadoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoResumoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.progresso.ProgressoTopicoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Disciplina;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Topico;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoAluno;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoDisciplina;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.progresso.ProgressoTopico;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusProgresso;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusTopico;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ResourceNotfoundException;
import com.alangodoy.studioApp.s.infrastructure.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgressoService {


    private final ProgressoAlunoRepository progressoAlunoRepository;
    private final ProgressoDisciplinaRepository progressoDisciplinaRepository;
    private final ProgressoTopicoRepository progressoTopicoRepository;
    private final AlunoRepository alunoRepository;
    private final InstrumentoRepository instrumentoRepository;
    private final ProgressoConverter progressoConverter;
    private final ProgressoAlunoResumoConverter progressoAlunoResumoConverter;
    private final DisciplinaRepository disciplinaRepository;
    private final TopicoRepository topicoRepository;
    private final ProgressoAlunoRepository progressoRepository;



    // ========== MÉTODOS PRINCIPAIS ========== //

    /**
     * Inicia uma nova progressão para um aluno em um instrumento específico
     */
    @Transactional
    public ProgressoAlunoResponseDTO iniciarProgressao(Long alunoId, Long instrumentoId) {

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotfoundException("Aluno não encontrado"));

        Instrumento instrumento = instrumentoRepository.findWithConteudoCompletoById(instrumentoId)
                .orElseThrow(() -> new ResourceNotfoundException("Instrumento não encontrado"));

        validarExistenciaProgressao(alunoId, instrumentoId);

        ProgressoAluno progressao = criarNovaProgressao(aluno, instrumento);
        ProgressoAluno salvo = progressoAlunoRepository.save(progressao);

        return progressoConverter.toProgressoAlunoDTO(salvo);
    }
    @Transactional
    public ProgressoTopicoResponseDTO iniciarTopico(Long alunoId, Long topicoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotfoundException("Aluno não encontrado"));

        // 2. Busca o ProgressoTopico existente
        ProgressoTopico progressoTopico = progressoTopicoRepository.findById(topicoId)
                .orElseThrow(() -> new ResourceNotfoundException("Progresso do tópico não encontrado"));

        // 3. Verifica se pertence ao aluno
        if (!progressoTopico.getProgressoDisciplina().getAluno().getId().equals(alunoId)) {
            throw new IllegalArgumentException("Este progresso não pertence ao aluno informado");
        }

        // 4. Busca ou cria ProgressoAluno (mantido por segurança)
        ProgressoAluno progressoAluno = progressoAlunoRepository.findByAlunoId(alunoId)
                .orElseGet(() -> {
                    ProgressoAluno novo = new ProgressoAluno();
                    novo.setAluno(aluno);
                    return progressoAlunoRepository.save(novo);
                });

        // 5. Atualiza o status (se não estiver concluído)
        if (progressoTopico.getStatus() != StatusTopico.TOPICO_CONCLUIDO) {
            progressoTopico.setStatus(StatusTopico.TOPICO_EM_ANDAMENTO);
            progressoTopico.setDataInicio(LocalDate.now());
            progressoTopico = progressoTopicoRepository.save(progressoTopico);
        }

        // 6. Atualiza a última atualização do ProgressoDisciplina
        ProgressoDisciplina progressoDisciplina = progressoTopico.getProgressoDisciplina();
        progressoDisciplina.setUltimaAtualizacao(LocalDateTime.now());
        progressoDisciplinaRepository.save(progressoDisciplina);
        return progressoConverter.toProgressoTopicoDTO(progressoTopico);
    }


    @Transactional
    public ProgressoTopicoResponseDTO concluirTopico(Long topicoId) {

        if (topicoId == null || topicoId <= 0) {
            throw new IllegalArgumentException("ID do tópico inválido");
        }

        // Verifica se o ID existe
        if (!progressoTopicoRepository.existsById(topicoId)) {
            throw new ResourceNotfoundException("Tópico com ID " + topicoId + " não encontrado");
        }

        ProgressoTopico topico = progressoTopicoRepository.findById(topicoId)
                .orElseThrow(() -> new ResourceNotfoundException("Tópico não encontrado"));
        topico.concluir();
        topico = progressoTopicoRepository.save(topico);

        atualizarProgressoDisciplina(topico.getProgressoDisciplina().getId());

        return progressoConverter.toProgressoTopicoDTO(topico);
    }

    public List<ProgressoDetalhadoResponseDTO.ProgressoDisciplinaDTO> buscarProximasDisciplinas(Long alunoId) {
        return progressoDisciplinaRepository.findById(alunoId).stream()
                .filter(d -> !d.isConcluido())
                .map(progressoConverter::toProgressoDisciplinaDTO)
                .collect(Collectors.toList());
    }

    public List<ProgressoTopicoResponseDTO> buscarProximosTopicos(Long alunoId) {
        return progressoTopicoRepository.findByProgressoDisciplina_AlunoId(alunoId).stream()
                .filter(t -> !t.isConcluido())
                .map(progressoConverter::toProgressoTopicoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProgressoTopicoResponseDTO marcarTopicoComoEmAndamento(Long topicoId) {
        ProgressoTopico topico = progressoTopicoRepository.findById(topicoId)
                .orElseThrow(() -> new ResourceNotfoundException("Tópico não encontrado"));

        topico.marcarComoEmAndamento();
        topico = progressoTopicoRepository.save(topico);

        atualizarProgressoDisciplina(topico.getProgressoDisciplina().getId());

        return progressoConverter.toProgressoTopicoDTO(topico);
    }

    @Transactional
    public void iniciarProgressoAluno(Long alunoId, Long disciplinaId, Long topicoId) {
        // 1. Valida se o aluno existe
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotfoundException("Aluno não encontrado"));

        // 2. Valida se a disciplina existe
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new ResourceNotfoundException("Disciplina não encontrada"));

        // 3. Valida se o tópico existe e pertence à disciplina
        Topico topico = topicoRepository.findByIdAndDisciplinaId(topicoId, disciplinaId)
                .orElseThrow(() -> new ResourceNotfoundException("Tópico não encontrado ou não pertence à disciplina"));

        // 4. Cria ou atualiza ProgressoDisciplina (se já existir)
        ProgressoDisciplina progressoDisciplina = progressoDisciplinaRepository
                .findByAlunoIdAndDisciplinaId(alunoId, disciplinaId)
                .orElseGet(() -> {
                    ProgressoDisciplina novo = new ProgressoDisciplina();
                    novo.setAluno(aluno);
                    novo.setDisciplina(disciplina);
                    return progressoDisciplinaRepository.save(novo);
                });

        // 5. Cria ProgressoTopico (se não existir)
        progressoTopicoRepository
                .findByProgressoDisciplinaIdAndTopicoId(progressoDisciplina.getId(), topicoId)
                .orElseGet(() -> {
                    ProgressoTopico novo = new ProgressoTopico();
                    novo.setProgressoDisciplina(progressoDisciplina);
                    novo.setTopico(topico);
                    novo.setStatus(StatusTopico.TOPICO_NAO_INICIADO); // Ou TOPICO_EM_ANDAMENTO
                    return progressoTopicoRepository.save(novo);
                });
    }

    public ProgressoTopicoResponseDTO atualizarStatusTopico(Long progressoTopicoId, StatusTopico novoStatus, String observacoes) {
        ProgressoTopico progressoTopico = buscarProgressoTopico(progressoTopicoId);

        atualizarProgressoTopicos(progressoTopico, novoStatus, observacoes);
        ProgressoTopico atualizado = progressoTopicoRepository.save(progressoTopico);

        atualizarProgressoDisciplina(progressoTopico.getProgressoDisciplina().getId());

        return progressoConverter.toProgressoTopicoDTO(atualizado);
    }

    // ========== MÉTODOS DE CONSULTA ========== //

    /**
     * Busca todas as progressões de um aluno (pode retornar lista vazia se não houver)
     */
    public List<ProgressoAlunoResponseDTO> buscarTodasProgressoesAluno(Long alunoId) {
        return progressoAlunoRepository.findByAlunoIdWithDetails(alunoId).stream()
                .map(progressoConverter::toProgressoAlunoDTO)
                .collect(Collectors.toList());
    }

    // ========== MÉTODOS PRIVADOS ========== //

    private ProgressoAluno criarNovaProgressao(Aluno aluno, Instrumento instrumento) {
        ProgressoAluno progressao = new ProgressoAluno();
        progressao.setAluno(aluno);
        progressao.setInstrumento(instrumento);
        progressao.setStatus(StatusProgresso.NAO_INICIADA);
        progressao.setDataInicio(LocalDate.now());
        progressao.setUltimaAtualizacao(LocalDateTime.now());
        progressao.setProgressoGeral(BigDecimal.ZERO);

        instrumento.getConteudoAtivo().getDisciplinas().forEach(disciplina -> {
            ProgressoDisciplina pd = criarProgressoDisciplina(progressao, disciplina);
            disciplina.getTopicos().forEach(topico -> pd.getTopicos().add(criarProgressoTopico(pd, topico)));
            progressao.getDisciplinas().add(pd);
        });

        return progressao;
    }
    private ProgressoDisciplina criarProgressoDisciplina(ProgressoAluno progressao, Disciplina disciplina) {
        // Verificação de nulidade importante
        if (progressao == null || progressao.getAluno() == null) {
            throw new IllegalArgumentException("ProgressoAluno ou Aluno associado não pode ser nulo");
        }

        ProgressoDisciplina pd = new ProgressoDisciplina();

        // Configuração correta das relações
        pd.setProgressoAluno(progressao);
        pd.setAluno(progressao.getAluno());
        pd.setDisciplina(disciplina);
        pd.setStatus(StatusProgresso.NAO_INICIADA);
        pd.setUltimaAtualizacao(LocalDateTime.now());
        pd.setProgresso(BigDecimal.ZERO);

        return pd;
    }


    private ProgressoTopico criarProgressoTopico(ProgressoDisciplina pd, Topico topico) {
        ProgressoTopico pt = new ProgressoTopico();
        pt.setProgressoDisciplina(pd);
        pt.setTopico(topico);
        pt.setStatus(StatusTopico.TOPICO_NAO_INICIADO);
        pt.setUltimaAtualizacao(LocalDateTime.now());
        pt.setProgresso(BigDecimal.ZERO);
        return pt;
    }

    private void validarExistenciaProgressao(Long alunoId, Long instrumentoId) {
        if (progressoAlunoRepository.existsByAlunoIdAndInstrumentoId(alunoId, instrumentoId)) {
            throw new IllegalStateException("Progressão já existe para este aluno e instrumento");
        }
    }

    private ProgressoTopico buscarProgressoTopico(Long progressoTopicoId) {
        return progressoTopicoRepository.findById(progressoTopicoId)
                .orElseThrow(() -> new ResourceNotfoundException("Progressão de tópico não encontrada"));
    }

    private void atualizarProgressoTopicos(ProgressoTopico progressoTopico, StatusTopico novoStatus, String observacoes) {
        progressoTopico.setStatus(novoStatus);
        progressoTopico.setObservacoes(observacoes);
        progressoTopico.setUltimaAtualizacao(LocalDateTime.now());
        progressoTopico.setProgresso(calcularProgressoPorStatus(novoStatus));
    }

    private BigDecimal calcularProgressoPorStatus(StatusTopico status) {
        return switch (status) {
            case TOPICO_CONCLUIDO -> BigDecimal.valueOf(100);
            case TOPICO_EM_ANDAMENTO -> BigDecimal.valueOf(50);
            default -> BigDecimal.ZERO;
        };
    }

    private void atualizarProgressoDisciplina(Long progressoDisciplinaId) {
        ProgressoDisciplina disciplina = progressoDisciplinaRepository.findByIdWithTopicos(progressoDisciplinaId)
                .orElseThrow(() -> new ResourceNotfoundException("Disciplina não encontrada"));
        disciplina.verificarConclusaoAutomatica();
        disciplina.calcularProgresso();
        progressoDisciplinaRepository.save(disciplina);
        atualizarProgressoAluno(disciplina.getProgressoAluno().getId());
    }


    private void atualizarProgressoAluno(Long progressoAlunoId) {
        ProgressoAluno progressao = progressoAlunoRepository.findByIdWithDisciplinas(progressoAlunoId)
                .orElseThrow(() -> new ResourceNotfoundException("Progressão não encontrada"));

        progressao.calcularProgressoGeral();
        progressao.calcularPercentualConclusao();
        progressoAlunoRepository.save(progressao);
    }


    public ProgressoDetalhadoResponseDTO obterProgressoDetalhado(Long alunoId) {
        List<ProgressoAluno> progressos = progressoAlunoRepository.findByAlunoIdWithDetails(alunoId);

        if (progressos.isEmpty()) {
            throw new ResourceNotfoundException("Nenhum progresso encontrado para este aluno");
        }

        return progressoConverter.toProgressoDetalhadoDTO(progressos);
    }



    // Método para atualizar progresso
    public void atualizarProgresso(StatusTopico novoStatus) {

        ProgressoTopico progressoTopico = new ProgressoTopico();
        progressoTopico.setStatus(novoStatus);
       progressoTopico.setUltimaAtualizacao(LocalDateTime.now());

        // Calcula progresso baseado no status
        switch (novoStatus) {
            case TOPICO_CONCLUIDO:
                progressoTopico.setProgresso(BigDecimal.valueOf(100));
                break;
            case TOPICO_EM_ANDAMENTO:
                progressoTopico.setProgresso(BigDecimal.valueOf(50));
                break;
            default:
                progressoTopico.setProgresso(BigDecimal.ZERO);
        }

        // Propaga atualização para a disciplina
        if (progressoTopico.getProgressoDisciplina() != null) {
            progressoTopico.getProgressoDisciplina().
                    calcularProgresso();
        }
    }



    /**
     * Busca a progressão completa de um aluno
     */
    public ProgressoAlunoResponseDTO buscarProgressaoAluno(Long alunoId) {
        ProgressoAluno progressao = progressoAlunoRepository.findByIdWithDetails(alunoId)
                .orElseThrow(() -> new ResourceNotfoundException("Progressão não encontrada"));

        return progressoConverter.toProgressoAlunoDTO(progressao);
    }

    /**
     * Obtém o resumo do progresso atual do aluno
     */
    public ProgressoResumoResponseDTO obterResumoProgresso(Long alunoId) {
        List<ProgressoAluno> progressoes = progressoAlunoRepository.findByAlunoIdWithDetails(alunoId);

        if (progressoes.isEmpty()) {
            throw new ResourceNotfoundException("Nenhuma progressão encontrada para este aluno");
        }

        ProgressoResumoResponseDTO resumo = new ProgressoResumoResponseDTO();
        resumo.setAlunoId(alunoId);
        resumo.setAlunoNome(progressoes.get(0).getAluno().getNome());

        // Calcula totais
        long totalDisciplinas = progressoes.stream()
                .flatMap(p -> p.getDisciplinas().stream())
                .count();

        long disciplinasConcluidas = progressoes.stream()
                .flatMap(p -> p.getDisciplinas().stream())
                .filter(d -> d.getStatus() == StatusProgresso.DISCIPLINA_CONCLUIDA)
                .count();

        long totalTopicos = progressoes.stream()
                .flatMap(p -> p.getDisciplinas().stream())
                .flatMap(d -> d.getTopicos().stream())
                .count();

        long topicosConcluidos = progressoes.stream()
                .flatMap(p -> p.getDisciplinas().stream())
                .flatMap(d -> d.getTopicos().stream())
                .filter(t -> t.getStatus() == StatusTopico.TOPICO_CONCLUIDO)
                .count();

        resumo.setTotalDisciplinas(totalDisciplinas);
        resumo.setDisciplinasConcluidas(disciplinasConcluidas);
        resumo.setTotalTopicos(totalTopicos);
        resumo.setTopicosConcluidos(topicosConcluidos);

        if (totalDisciplinas > 0) {
            resumo.setProgressoGeral(BigDecimal.valueOf(disciplinasConcluidas)
                    .divide(BigDecimal.valueOf(totalDisciplinas), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)));
        } else {
            resumo.setProgressoGeral(BigDecimal.ZERO);
        }

        return resumo;
    }


}

