package com.alangodoy.studioApp.s.business.services;

import com.alangodoy.studioApp.s.business.converter.aluno.ProfessorConverter;
import com.alangodoy.studioApp.s.business.dto.in.aluno.ProfessorRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.ProfessorResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.EntityUpdate;
import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.alangodoy.studioApp.s.infrastructure.entity.Professor;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ConflitException;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ResourceNotfoundException;
import com.alangodoy.studioApp.s.infrastructure.repository.InstrumentoRepository;
import com.alangodoy.studioApp.s.infrastructure.repository.ProfessorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private ProfessorConverter professorConverter;
    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Transactional
    public ProfessorResponseDTO cadastrarProfessor(ProfessorRequestDTO professorDTO) {
        // Verifica se o professor já existe por CPF
        if (professorRepository.existsByCpf(professorDTO.getCpf())) {
            throw new ConflitException("Já existe um professor cadastrado com este CPF");
        }


        // Converte DTO para entidade
        Professor professor = new Professor();
        professor.setNome(professorDTO.getNome());
        professor.setEmail(professorDTO.getEmail());
        professor.setCpf(professorDTO.getCpf());
        professor.setTelefone(professorDTO.getTelefone());

        // Busca e associa todos os instrumentos se a lista estiver vazia (caso atual)
        if (professorDTO.getInstrumentosIds().isEmpty()) {
            List<Instrumento> todosInstrumentos = instrumentoRepository.findAll();
            professor.setInstrumentos(new HashSet<>(todosInstrumentos));
        } else {
            // Caso futuro onde se especifica quais instrumentos o professor ensina
            Set<Instrumento> instrumentos = instrumentoRepository.findAllById(professorDTO.getInstrumentosIds())
                    .stream().collect(Collectors.toSet());
            professor.setInstrumentos(instrumentos);
        }

        // Salva no banco
        Professor professorSalvo = professorRepository.save(professor);

        // Converte para DTO de resposta
        return professorConverter.toDTO(professorSalvo);
    }

    public Professor encontrarProfessorParaInstrumento(Long instrumentoId) {
        // 1. Busca professores específicos ou genéricos
        List<Professor> professores = professorRepository
                .findProfessoresDisponiveisParaInstrumento(instrumentoId);

        // 2. Se não encontrar, tenta professores genéricos como fallback
        if (professores.isEmpty()) {
            professores = professorRepository.findProfessoresGenericos();
        }

        // 3. Último fallback: qualquer professor disponível
        if (professores.isEmpty()) {
            professores = professorRepository.findAll();
        }

        if (professores.isEmpty()) {
            throw new ResourceNotfoundException("Não há professores disponíveis na escola");
        }


        // Retorna o primeiro professor disponível (sem balanceamento de carga)
        return professores.get(0);
    }

    public List<ProfessorResponseDTO> listarTodos () {
        return professorRepository.findAll()
                .stream()
                .map(professorConverter::toDTO)  // Usando o construtor especial
                .collect(Collectors.toList());
    }

    @Transactional
    public ProfessorResponseDTO atualizarParcialmente(Long id, Map<String, Object> updates) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Professor não encontrada"));

        // Validação especial para nome único
        if (updates.containsKey("nome")) {
            String novoNome = (String) updates.get("nome");
            if (!professor.getNome().equals(novoNome)) {
                if (professorRepository.existsByNome(novoNome)) {
                    throw new ConflitException("Já existe este nome");
                }
            }
        }

        try {
            EntityUpdate.updatePartialEntity(professor, updates);
            return professorConverter.toDTO(professorRepository.save(professor));
        } catch (IllegalAccessException e) {
            throw new ConflitException("Erro ao atualizar categoria: " + e.getMessage());
        }
    }



}
