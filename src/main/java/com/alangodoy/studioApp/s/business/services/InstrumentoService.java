package com.alangodoy.studioApp.s.business.services;


import com.alangodoy.studioApp.s.business.converter.instrumento.InstrumentoConverter;
import com.alangodoy.studioApp.s.business.dto.in.instrumento.InstrumentoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.instrumento.InstrumentoResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.EntityUpdate;
import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.alangodoy.studioApp.s.infrastructure.enums.InstrumentoTipo;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ConflitException;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ResourceNotfoundException;
import com.alangodoy.studioApp.s.infrastructure.repository.InstrumentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrumentoService {

    private final InstrumentoRepository instrumentoRepository;
    private final InstrumentoConverter instrumentoConverter;
    @Transactional
    public InstrumentoResponseDTO cadastrarInstrumento(InstrumentoRequestDTO instrumentoDTO) {
        // Valida se o instrumento já existe pelo nome
        if (instrumentoRepository.existsByNomeIgnoreCase(instrumentoDTO.getNome())) {
            throw new ConflitException("Já existe um instrumento com este nome");
        }

        // Converte DTO para entidade
        Instrumento instrumento = instrumentoConverter.toInstrumentoEntity(instrumentoDTO);

        // Persiste no banco de dados
        Instrumento instrumentoSalvo = instrumentoRepository.save(instrumento);

        // Converte para DTO de resposta
        return instrumentoConverter.toInstrumentoDTO(instrumentoSalvo);
    }

    public List<InstrumentoResponseDTO> listarPorTipo(InstrumentoTipo categoria) {
        return instrumentoConverter.toDTOList(instrumentoRepository.findByTipo(categoria));
    }


    public List<InstrumentoResponseDTO> listarTodos() {
        // Busca todos os instrumentos do banco
        List<Instrumento> instrumentos = instrumentoRepository.findAll();

        // Converte cada Instrumento para InstrumentoDTO
        return instrumentos.stream()
                .map(instrumentoConverter::toInstrumentoDTO)
                .collect(Collectors.toList());


    }

    @Transactional
    public InstrumentoResponseDTO atualizarParcialmente(Long id, Map<String, Object> updates) {
        Instrumento categoria = instrumentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Categoria não encontrada"));

        // Validação especial para nome único
        if (updates.containsKey("nome")) {
            String novoNome = (String) updates.get("nome");
            if (!categoria.getNome().equals(novoNome)) {
                if (instrumentoRepository.existsByNome(novoNome)) {
                    throw new ConflitException("Já existe uma categoria com este nome");
                }
            }
        }

        try {
            EntityUpdate.updatePartialEntity(categoria, updates);
            return instrumentoConverter.toInstrumentoDTO(instrumentoRepository.save(categoria));
        } catch (IllegalAccessException e) {
            throw new ConflitException("Erro ao atualizar categoria: " + e.getMessage());
        }
    }
}


