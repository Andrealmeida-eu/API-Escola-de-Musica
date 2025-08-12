package com.alangodoy.studioApp.s.business.services;

import com.alangodoy.studioApp.s.business.converter.aluno.RelatorioAulaConverter;
import com.alangodoy.studioApp.s.business.dto.in.RelatorioAulaRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.RelatorioAulaResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.EntityUpdate;
import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.Professor;
import com.alangodoy.studioApp.s.infrastructure.entity.RelatorioAula;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ConflitException;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ResourceNotfoundException;
import com.alangodoy.studioApp.s.infrastructure.repository.AlunoRepository;
import com.alangodoy.studioApp.s.infrastructure.repository.ProfessorRepository;
import com.alangodoy.studioApp.s.infrastructure.repository.RelatorioAulaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioAulaService {


    private final RelatorioAulaRepository relatorioAulaRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final RelatorioAulaConverter relatorioAulaConverter;


    public RelatorioAulaResponseDTO criarRelatorio(RelatorioAulaRequestDTO dto) {

        Professor professor = professorRepository.findById(dto.getProfessorId())
                .orElseThrow(() -> new ResourceNotfoundException("Professor não encontrado"));

        RelatorioAula relatorio = new RelatorioAula();
        relatorio.setDataAula(dto.getDataAula());
        relatorio.setProfessor(professor);
        relatorio.setDescricao(dto.getDescricao());

        if (dto.getAlunoId() != null) {
            Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                    .orElseThrow(() -> new ResourceNotfoundException("Aluno não encontrado"));
            relatorio.setAluno(aluno);
        }


        RelatorioAula saved = relatorioAulaRepository.save(relatorio);
        return relatorioAulaConverter.toDTO(saved);
    }

    public List<RelatorioAulaResponseDTO> listarPorAlunoId(Long alunoId) {
        List<RelatorioAula> relatorios;
        relatorios = relatorioAulaRepository.findByAlunoId(alunoId);

        return relatorios.stream()
                .map(relatorioAulaConverter::toDTO)
                .collect(Collectors.toList());
    }


    public List<RelatorioAulaResponseDTO> listarRelatorios(
            Long professorId, Long alunoId, LocalDate dataInicio, LocalDate dataFim) {

        List<RelatorioAula> relatorios;

        if (professorId != null && dataInicio != null && dataFim != null) {
            relatorios = relatorioAulaRepository.findRelatoriosPorProfessorEPeriodo(professorId, dataInicio, dataFim);
        } else if (professorId != null) {
            relatorios = relatorioAulaRepository.findByProfessorId(professorId);
        } else if (alunoId != null) {
            relatorios = relatorioAulaRepository.findByAlunoId(alunoId);
        } else if (dataInicio != null && dataFim != null) {
            relatorios = relatorioAulaRepository.findByDataAulaBetween(dataInicio, dataFim);
        } else {
            relatorios = relatorioAulaRepository.findAll();
        }

        return relatorios.stream()
                .map(relatorioAulaConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RelatorioAulaResponseDTO atualizarParcialmente(Long id, Map<String, Object> updates) {
        RelatorioAula categoria = relatorioAulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Categoria não encontrada"));

        try {
            EntityUpdate.updatePartialEntity(categoria, updates);
            return relatorioAulaConverter.toDTO(relatorioAulaRepository.save(categoria));
        } catch (IllegalAccessException e) {
            throw new ConflitException("Erro ao atualizar categoria: " + e.getMessage());
        }
    }



}
