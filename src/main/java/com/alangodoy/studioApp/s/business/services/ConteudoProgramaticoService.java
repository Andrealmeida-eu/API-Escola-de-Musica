package com.alangodoy.studioApp.s.business.services;

import com.alangodoy.studioApp.s.business.converter.conteudo.ConteudoProgramaticoConverter;
import com.alangodoy.studioApp.s.business.converter.conteudo.DisciplinaConverter;
import com.alangodoy.studioApp.s.business.converter.conteudo.TopicoConverter;
import com.alangodoy.studioApp.s.business.dto.in.conteudo.ConteudoProgramaticoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.in.conteudo.DisciplinaRequestDTO;
import com.alangodoy.studioApp.s.business.dto.in.conteudo.TopicoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.ConteudoProgramaticoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.DisciplinaResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.conteudo.TopicoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.EntityUpdate;
import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.ConteudoProgramatico;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Disciplina;
import com.alangodoy.studioApp.s.infrastructure.entity.pedagogico.grade.Topico;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ConflitException;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ResourceNotfoundException;
import com.alangodoy.studioApp.s.infrastructure.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ConteudoProgramaticoService {

    private final InstrumentoRepository instrumentoRepository;
    private final ConteudoProgramaticoRepository conteudoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final TopicoRepository topicoRepository;
    private final ConteudoProgramaticoConverter conteudoMapper;
    private final ProgressoTopicoRepository progressoTopicoRepository;
    private final DisciplinaConverter disciplinaConverter;
    private final TopicoConverter topicoConverter;

    // ========== OPERAÇÕES PRINCIPAIS ========== //

    /**
     * Cria um novo conteúdo programático para um instrumento
     */
    public ConteudoProgramaticoResponseDTO criarConteudoCompleto(Long instrumentoId, ConteudoProgramaticoRequestDTO dto) {
        Instrumento instrumento = buscarInstrumentoValido(instrumentoId);
        validarConteudoExistente(instrumento);

        ConteudoProgramatico conteudo = conteudoMapper.toCPEntity(dto, instrumento);
        conteudo.setInstrumento(instrumento);

        ConteudoProgramatico conteudoSalvo = conteudoRepository.save(conteudo);
        return conteudoMapper.toCPDTO(conteudoSalvo);
    }

    /**
     * Atualiza todo o conteúdo programático de um instrumento
     */
    public ConteudoProgramaticoResponseDTO atualizarConteudoCompleto(Long instrumentoId, ConteudoProgramaticoRequestDTO dto) {
        Instrumento instrumento = buscarInstrumentoValido(instrumentoId);
        ConteudoProgramatico conteudoExistente = instrumento.getConteudoAtivo();

        if (conteudoExistente == null) {
            throw new ResourceNotfoundException("Instrumento não possui conteúdo programático para atualizar");
        }

        // Atualiza propriedades básicas
        conteudoMapper.updateFromDTO(dto, conteudoExistente);

        // Processa hierarquia completa
        processarAtualizacaoDisciplinas(conteudoExistente, dto.getDisciplinas());

        ConteudoProgramatico conteudoAtualizado = conteudoRepository.save(conteudoExistente);
        return conteudoMapper.toCPDTO(conteudoAtualizado);
    }

    // ========== OPERAÇÕES DE DISCIPLINAS ========== //

    /**
     * Adiciona uma nova disciplina ao conteúdo existente
     */
    public DisciplinaResponseDTO adicionarDisciplina(Long instrumentoId, DisciplinaRequestDTO disciplinaDTO) {
        ConteudoProgramatico conteudo = buscarConteudoPorInstrumentoId(instrumentoId);

        Disciplina disciplina = new Disciplina();
        disciplina.setNome(disciplinaDTO.getNome());
        disciplina.setDescricao(disciplinaDTO.getDescricao());
        disciplina.setOrdem(disciplinaDTO.getOrdem());
        disciplina.setConteudo(conteudo);

        Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);
        return disciplinaConverter.toDisciplinaDTO(disciplinaSalva);
    }

    /**
     * Atualiza uma disciplina existente
     */
    public DisciplinaResponseDTO atualizarDisciplina(Long disciplinaId, DisciplinaRequestDTO disciplinaDTO) {
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new ResourceNotfoundException("Disciplina não encontrada"));

        disciplina.setNome(disciplinaDTO.getNome());
        disciplina.setDescricao(disciplinaDTO.getDescricao());
        disciplina.setOrdem(disciplinaDTO.getOrdem());

        Disciplina disciplinaAtualizada = disciplinaRepository.save(disciplina);
        return disciplinaConverter.toDisciplinaDTO(disciplinaAtualizada);
    }

    @Transactional
    public void inativarDisciplina(Long disciplinaId) {
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado"));

        disciplina.setAtivo(false);

        for (Topico topico : disciplina.getTopicos()) {
            topico.setAtivo(false);
        }
        disciplinaRepository.save(disciplina); // O cascade deve cuidar de salvar as mudanças nos filhos
    }

    public List<DisciplinaResponseDTO> listarTodas() {
        return disciplinaRepository.findAll().stream()
                .map(disciplinaConverter::toDisciplinaDTO)
                .collect(Collectors.toList());
    }

    // ========== OPERAÇÕES DE TÓPICOS ========== //

    /**
     * Adiciona um novo tópico a uma disciplina
     */
    public TopicoResponseDTO adicionarTopico(Long disciplinaId, TopicoRequestDTO topicoDTO) {
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new ResourceNotfoundException("Disciplina não encontrada"));

        Topico topico = new Topico();
        topico.setNome(topicoDTO.getNome());
        topico.setOrdem(topicoDTO.getOrdem());
        topico.setDisciplina(disciplina);

        Topico topicoSalvo = topicoRepository.save(topico);
        return topicoConverter.toTopicoDTO(topicoSalvo);
    }

    /**
     * Atualiza um tópico existente
     */
    public TopicoResponseDTO atualizarTopico(Long topicoId, TopicoRequestDTO topicoDTO) {
        Topico topico = topicoRepository.findById(topicoId)
                .orElseThrow(() -> new ResourceNotfoundException("Tópico não encontrado"));

        topico.setNome(topicoDTO.getNome());
        topico.setOrdem(topicoDTO.getOrdem());

        Topico topicoAtualizado = topicoRepository.save(topico);
        return topicoConverter.toTopicoDTO(topicoAtualizado);
    }

    @Transactional
    public void inativarTopico(Long topicoId) {
        Topico conteudo = topicoRepository.findById(topicoId)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado"));

        conteudo.setAtivo(false);
        topicoRepository.save(conteudo); // O cascade deve cuidar de salvar as mudanças nos filhos
    }

    public List<TopicoResponseDTO> listarPorDisciplina(Long disciplinaId) {
        return topicoRepository.findByDisciplinaId(disciplinaId).stream()
                .map(topicoConverter::toTopicoDTO)
                .collect(Collectors.toList());
    }

    // ========== CONSULTAS ========== //

    /**
     * Busca o conteúdo completo de um instrumento
     */
    public ConteudoProgramaticoResponseDTO buscarConteudoCompleto(Long instrumentoId) {
        Instrumento instrumento = instrumentoRepository.findWithConteudoCompletoById(instrumentoId)
                .orElseThrow(() -> new ResourceNotfoundException("Instrumento não encontrado"));

        return conteudoMapper.toCPDTO(instrumento.getConteudoAtivo());
    }

    /**
     * Lista todos os conteúdos programáticos (para administração)
     */
    public List<ConteudoProgramaticoResponseDTO> listarTodosConteudos() {
        return conteudoRepository.findAll().stream()
                .map(conteudoMapper::toCPDTO)
                .collect(Collectors.toList());
    }

    // ========== MÉTODOS PRIVADOS ========== //

    private Instrumento buscarInstrumentoValido(Long instrumentoId) {
        return instrumentoRepository.findById(instrumentoId)
                .orElseThrow(() -> new ResourceNotfoundException("Instrumento não encontrado"));
    }

    private void validarConteudoExistente(Instrumento instrumento) {
        if (conteudoRepository.existsByInstrumentoAndAtivoTrue(instrumento)) {
            throw new ConflitException("Instrumento já possui conteúdo programático ativo");
        }
    }

    private ConteudoProgramatico buscarConteudoPorInstrumentoId(Long instrumentoId) {
        return instrumentoRepository.findWithConteudoById(instrumentoId)
                .map(Instrumento::getConteudoAtivo)
                .orElseThrow(() -> new ResourceNotfoundException("Conteúdo programático não encontrado"));
    }

    private void processarAtualizacaoDisciplinas(ConteudoProgramatico conteudo, List<DisciplinaRequestDTO> disciplinasDTO) {
        // Mapeia disciplinas existentes para atualização
        Map<Long, Disciplina> disciplinasExistentes = conteudo.getDisciplinas().stream()
                .collect(Collectors.toMap(Disciplina::getId, Function.identity()));

        // Processa cada disciplina do DTO
        disciplinasDTO.forEach(disciplinaDTO -> {
            if (disciplinaDTO.getId() != null && disciplinasExistentes.containsKey(disciplinaDTO.getId())) {
                // Atualiza disciplina existente
                Disciplina disciplina = disciplinasExistentes.get(disciplinaDTO.getId());
                disciplina.setNome(disciplinaDTO.getNome());
                disciplina.setDescricao(disciplinaDTO.getDescricao());
                disciplina.setOrdem(disciplinaDTO.getOrdem());
                processarAtualizacaoTopicos(disciplina, disciplinaDTO.getTopicos());
            } else {
                // Cria nova disciplina
                Disciplina novaDisciplina = new Disciplina();
                novaDisciplina.setNome(disciplinaDTO.getNome());
                novaDisciplina.setDescricao(disciplinaDTO.getDescricao());
                novaDisciplina.setOrdem(disciplinaDTO.getOrdem());
                novaDisciplina.setConteudo(conteudo);

                // Cria tópicos
                disciplinaDTO.getTopicos().forEach(topicoDTO -> {
                    Topico topico = new Topico();
                    topico.setNome(topicoDTO.getNome());
                    topico.setOrdem(topicoDTO.getOrdem());
                    topico.setDisciplina(novaDisciplina);
                    novaDisciplina.getTopicos().add(topico);
                });

                conteudo.getDisciplinas().add(novaDisciplina);
            }
        });

        // Remove disciplinas não incluídas no DTO
        List<Long> idsDisciplinas = disciplinasDTO.stream()
                .filter(d -> d.getId() != null)
                .map(DisciplinaRequestDTO::getId)
                .collect(Collectors.toList());

        conteudo.getDisciplinas().removeIf(d -> !idsDisciplinas.contains(d.getId()));
    }

    private void processarAtualizacaoTopicos(Disciplina disciplina, List<TopicoRequestDTO> topicosDTO) {
        Map<Long, Topico> topicosExistentes = disciplina.getTopicos().stream()
                .collect(Collectors.toMap(Topico::getId, Function.identity()));

        topicosDTO.forEach(topicoDTO -> {
            if (topicoDTO.getId() != null && topicosExistentes.containsKey(topicoDTO.getId())) {
                // Atualiza tópico existente
                Topico topico = topicosExistentes.get(topicoDTO.getId());
                topico.setNome(topicoDTO.getNome());
                topico.setOrdem(topicoDTO.getOrdem());
            } else {
                // Cria novo tópico
                Topico novoTopico = new Topico();
                novoTopico.setNome(topicoDTO.getNome());
                novoTopico.setOrdem(topicoDTO.getOrdem());
                novoTopico.setDisciplina(disciplina);
                disciplina.getTopicos().add(novoTopico);
            }
        });
    }


   @Transactional
    public void inativarConteudo(Long conteudoId) {
        ConteudoProgramatico conteudo = conteudoRepository.findById(conteudoId)
                .orElseThrow(() -> new EntityNotFoundException("Conteúdo não encontrado"));

        conteudo.setAtivo(false);

        for (Disciplina disciplina : conteudo.getDisciplinas()) {
            disciplina.setAtivo(false);

            for (Topico topico : disciplina.getTopicos()) {
                topico.setAtivo(false);
            }
        }

        conteudoRepository.save(conteudo); // O cascade deve cuidar de salvar as mudanças nos filhos
    }


    @Transactional
    public ConteudoProgramaticoResponseDTO atualizarParcialmente(Long id, Map<String, Object> updates) {
        ConteudoProgramatico conteudo = conteudoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Conteudo não encontrado"));

        try {
            EntityUpdate.updatePartialEntity(conteudo, updates);
            return conteudoMapper.toCPDTO(conteudoRepository.save(conteudo));
        } catch (IllegalAccessException e) {
            throw new ConflitException("Erro ao atualizar categoria: " + e.getMessage());
        }
    }


}