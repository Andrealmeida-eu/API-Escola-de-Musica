package com.alangodoy.studioApp.s.business.services;


import com.alangodoy.studioApp.s.business.converter.aluno.AlunoConverter;
import com.alangodoy.studioApp.s.business.converter.aluno.AulaConverter;
import com.alangodoy.studioApp.s.business.dto.in.aluno.AlunoRequestDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.AlunoResponseDTO;
import com.alangodoy.studioApp.s.business.dto.out.aluno.AulaResponseDTO;
import com.alangodoy.studioApp.s.infrastructure.EntityUpdate;
import com.alangodoy.studioApp.s.infrastructure.entity.Aluno;
import com.alangodoy.studioApp.s.infrastructure.entity.Aula;
import com.alangodoy.studioApp.s.infrastructure.entity.Instrumento;
import com.alangodoy.studioApp.s.infrastructure.entity.Professor;
import com.alangodoy.studioApp.s.infrastructure.entity.financeiro.Mensalidade;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusAula;
import com.alangodoy.studioApp.s.infrastructure.enums.StatusMensalidade;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ConflitException;
import com.alangodoy.studioApp.s.infrastructure.exceptions.HorarioIndisponivelException;
import com.alangodoy.studioApp.s.infrastructure.exceptions.ResourceNotfoundException;
import com.alangodoy.studioApp.s.infrastructure.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlunoService {

    private final AulaRepository aulaRepository;
    private final AulaConverter aulaConverter;
    private final AlunoRepository alunoRepository;
    private final AlunoConverter alunoConverter;
    private final ProgressoAlunoRepository progressoAlunoRepository;
    private final MensalidadeRepository mensalidadeRepository;
    private final InstrumentoRepository instrumentoRepository;
    private final ProfessorRepository professorRepository;
    private final ProgressoService progressoService;
    private final ProfessorService professorService;

    @Transactional
    public AlunoResponseDTO cadastrarAluno(@Valid AlunoRequestDTO alunoDTO) {
        log.info("Aluno recebido: {}", alunoDTO.getNome());

        validarHorarioDisponivel(alunoDTO);

        Instrumento instrumento = buscarEAtualizarInstrumento(alunoDTO.getInstrumentoId());
        log.info("Instrumento recebido: {}", instrumento.getId());
        Professor professor = professorService.encontrarProfessorParaInstrumento(instrumento.getId());
        log.info("Professor recebido: {}", professor.getId());
        // Verifica conflito de horário com o professor
        validarDisponibilidadeProfessor(professor, alunoDTO.getDiaSemanaAula(), alunoDTO.getHorarioAula());

        Aluno alunoSalvo = salvarAluno(alunoDTO, instrumento, professor);

        System.out.println("Horário Aua: " + alunoDTO.getDiaSemanaAula());
        // Cria a aula inicial marcando como recorrente
        criarRecorrenciaDeAulas(alunoSalvo, professor, alunoDTO.getDiaSemanaAula(), alunoDTO.getHorarioAula());
        log.info("Aluno Salvo: {}", alunoSalvo.getId());

        Mensalidade mensalidade = criarMensalidade(alunoSalvo);

        atualizarAlunoComMensalidade(alunoSalvo, mensalidade);

        // Cria apenas a PRIMEIRA aula (as demais serão geradas dinamicamente)
        // criarPrimeiraAula(alunoSalvo, professor);

        progressoService.iniciarProgressao(alunoSalvo.getId(), instrumento.getId());

        return alunoConverter.toDTO(alunoSalvo);
    }

    public void deletarAluno(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("Aluno não encontrado"));
        alunoRepository.delete(aluno);
    }

    public AlunoResponseDTO buscarPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
        return alunoConverter.toDTO(aluno);

    }

    public List<AulaResponseDTO> listarAulasPorAluno(Long alunoId) {
        // Verifica se o aluno existe
        if (!alunoRepository.existsById(alunoId)) {
            throw new ResourceNotfoundException("Aluno não encontrado com ID: " + alunoId);
        }

        // Busca as aulas no repositório
        List<Aula> aulas = aulaRepository.findByAlunoIdOrderByDataHoraAsc(alunoId);


        // Converte para DTO usando o converter específico
        return aulaConverter.toDTOList(aulas);
    }


    public List<AlunoResponseDTO> listarTodos() {
        return alunoRepository.findAll()
                .stream()
                .map(alunoConverter::toDTO)  // Usando o construtor especial
                .collect(Collectors.toList());
    }


    @Transactional
    public AlunoResponseDTO atualizarParcialmente(Long id, Map<String, Object> updates) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfoundException("aluno não encontrada"));
        // Validação especial para nome único
        if (updates.containsKey("nome")) {
            String novoNome = (String) updates.get("nome");
            if (!aluno.getNome().equals(novoNome)) {
                if (alunoRepository.existsByNome(novoNome)) {
                    throw new ConflitException("Já existe uma aluno com este nome");
                }
            }
        }
        // Tratamento especial para instrumentoId
        if (updates.containsKey("instrumentoId")) {
            Long instrumentoId = Long.valueOf(updates.get("instrumentoId").toString());
            Instrumento instrumento = instrumentoRepository.findById(instrumentoId)
                    .orElseThrow(() -> new ResourceNotfoundException("Instrumento não encontrado"));
            aluno.setInstrumento(instrumento);
            updates.remove("instrumentoId"); // Remove para não tentar atualizar via reflection
        }

        // Tratamento especial para diaSemanaAula e horarioAula - aplica a TODAS as aulas
        if ((updates.containsKey("diaSemanaAula") || updates.containsKey("horarioAula"))
                && !aluno.getAulas().isEmpty()) {

            DayOfWeek novoDia = updates.containsKey("diaSemanaAula")
                    ? DayOfWeek.valueOf(updates.get("diaSemanaAula").toString())
                    : null;

            LocalTime novoHorario = updates.containsKey("horarioAula")
                    ? LocalTime.parse(updates.get("horarioAula").toString())
                    : null;

            // Atualiza todas as aulas do aluno
            for (Aula aula : aluno.getAulas()) {
                if (novoDia != null) {
                    aula.setDiaSemanaAula(novoDia);
                }
                if (novoHorario != null) {
                    aula.setHorarioAula(novoHorario);
                }
            }

            updates.remove("diaSemanaAula");
            updates.remove("horarioAula");
        }
        try {
            EntityUpdate.updatePartialEntity(aluno, updates);
            return alunoConverter.toDTO(alunoRepository.save(aluno));
        } catch (IllegalAccessException e) {
            throw new ConflitException("Erro ao atualizar aluno: " + e.getMessage());
        }
    }


    private void validarHorarioDisponivel(AlunoRequestDTO alunoDTO) {
        boolean ocupado = aulaRepository.existsByDiaSemanaAulaAndHorarioAula(
                alunoDTO.getDiaSemanaAula(), alunoDTO.getHorarioAula());

        if (ocupado) {
            throw new HorarioIndisponivelException("Horário já ocupado por outro aluno");
        }
    }

    private Instrumento buscarEAtualizarInstrumento(Long instrumentoId) {
        Instrumento instrumento = instrumentoRepository.findById(instrumentoId)
                .orElseThrow(() -> new ResourceNotfoundException("Instrumento não encontrado"));
        log.info("Instrumento id: " + instrumento.getId());
        instrumento.incrementarQuantidadeAlunos();
        return instrumentoRepository.save(instrumento);
    }

    private Aluno salvarAluno(AlunoRequestDTO dto, Instrumento instrumento, Professor professor) {
        Aluno aluno = alunoConverter.toEntity(dto);
        aluno.setProfessor(professor);
        aluno.setInstrumento(instrumento);
        aluno.setNome(dto.getNome());
        aluno.setCpf(dto.getCpf());
        aluno.setEmail(dto.getEmail());
        aluno.setTelefone(dto.getTelefone());

        return alunoRepository.save(aluno);
    }

    private Mensalidade criarMensalidade(Aluno aluno) {
        Mensalidade mensalidade = new Mensalidade();
        mensalidade.setValor(new BigDecimal("300.00"));
        mensalidade.setStatus(StatusMensalidade.ABERTA);
        mensalidade.setDataVencimento(LocalDate.now().withDayOfMonth(10)); // padrão: dia 10 do mês atual
        mensalidade.setAluno(aluno);
        mensalidade.setAno(LocalDate.now().getYear());
        return mensalidadeRepository.save(mensalidade);
    }

    private void atualizarAlunoComMensalidade(Aluno aluno, Mensalidade mensalidade) {
        if (aluno.getMensalidades() == null) {
            aluno.setMensalidades(new HashSet<>());
        }
        mensalidade.setAluno(aluno);  // Importante para manter o lado proprietário da relação
        aluno.getMensalidades().add(mensalidade);
        alunoRepository.save(aluno);
    }

    private void criarAulaFixa(AlunoRequestDTO dto, Aluno aluno, Professor professor) {
        Aula aula = new Aula();
        aula.setAluno(aluno);
        aula.setProfessor(professor);
        aula.setDiaSemanaAula(dto.getDiaSemanaAula());
        aula.setHorarioAula(dto.getHorarioAula());

        // Obtém o próximo dia da semana correspondente no mês e ano atuais
        LocalDate dataAula = getProximaDataNoMesCorrente(dto.getDiaSemanaAula());

        // Combina data e hora
        LocalDateTime dataHora = LocalDateTime.of(dataAula, dto.getHorarioAula());
        aula.setDataHora(dataHora);
        aula.setStatus(StatusAula.AGENDADA);
        aula.setObservacoes(""); // padrão

        aulaRepository.save(aula);
    }

    private LocalDate getProximaDataNoMesCorrente(DayOfWeek diaSemanaDesejado) {
        YearMonth mesAtual = YearMonth.now();
        LocalDate primeiroDiaDoMes = mesAtual.atDay(1);

        // Itera do primeiro ao último dia do mês até encontrar o primeiro dia da semana correspondente
        for (int i = 0; i < mesAtual.lengthOfMonth(); i++) {
            LocalDate data = primeiroDiaDoMes.plusDays(i);
            if (data.getDayOfWeek() == diaSemanaDesejado) {
                return data;
            }
        }

        // Fallback de segurança, nunca deverá ser alcançado
        throw new IllegalStateException("Dia da semana não encontrado no mês atual.");
    }

    private void validarDisponibilidadeProfessor(Professor professor, DayOfWeek diaSemana, LocalTime horario) {
        // Verifica se o professor já tem aula no mesmo horário
        boolean horarioOcupado = aulaRepository.existsByProfessorAndDiaSemanaAulaAndHorarioAula(
                professor,
                diaSemana,
                horario
        );

        if (horarioOcupado) {
            throw new ConflitException("O professor já tem aula agendada neste dia e horário");
        }

        // Aqui você pode adicionar outras validações, como:
        // - Horário comercial
        // - Disponibilidade específica do professor
        // - Feriados
    }

    private void criarPrimeiraAula(Aluno aluno, Professor professor) {

        AlunoResponseDTO dto = new AlunoResponseDTO();


        LocalDate primeiraData = LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(dto.getDiaSemanaAula()));

        LocalDateTime dataHora = LocalDateTime.of(primeiraData, dto.getHorarioAula());

        Aula aula = new Aula();

        aula.validarHorario();

        aula.setAluno(aluno);
        aula.setProfessor(professor);
        aula.setDataHora(dataHora);
        aula.setStatus(StatusAula.AGENDADA);
        aula.setObservacoes("Aula inicial do plano de ensino");

        aulaRepository.save(aula);
    }


    private void criarRecorrenciaDeAulas(Aluno aluno, Professor professor, DayOfWeek diaSemana, LocalTime horarioAula) {

        LocalDate hoje = LocalDate.now();
        LocalDate dataInicial = hoje.with(TemporalAdjusters.nextOrSame(diaSemana));
        LocalDate dataFinal = dataInicial.plusMonths(3); // Gera aulas para 3 meses

        for (LocalDate data = dataInicial; !data.isAfter(dataFinal); data = data.plusWeeks(1)) {
            LocalDateTime dataHoraAula = LocalDateTime.of(data, horarioAula);

            // Verifica se já não existe uma aula nesse horário (evitar duplicação)
            if (!aulaRepository.existsByAlunoAndDataHora(aluno, dataHoraAula)) {
                Aula aula = new Aula();
                aula.setAluno(aluno);
                aula.setProfessor(professor);
                aula.setDataHora(dataHoraAula);
                aula.setDiaSemanaAula(diaSemana);
                aula.setHorarioPadrao(horarioAula);
                aula.setHorarioAula(horarioAula);
                aula.setObservacoes("Aula inicial do plano de ensino");
                aula.setRecorrente(true);
                aula.setStatus(StatusAula.AGENDADA);

                aulaRepository.save(aula);
            }
        }
    }

    @Scheduled(cron = "0 0 2 1 * ?")
    @Transactional
    public void gerarProximasAulasRecorrentes() {
        // Busca alunos com aulas recorrentes que têm aulas futuras
        List<Aluno> alunosComAulasRecorrentes = alunoRepository.findByAulasRecorrenteTrue();

        for (Aluno aluno : alunosComAulasRecorrentes) {
            // Pega a última aula agendada do aluno
            Optional<Aula> ultimaAula = aulaRepository.findTopByAlunoOrderByDataHoraDesc(aluno);

            if (ultimaAula.isPresent()) {
                Aula aula = ultimaAula.get();
                LocalDate dataUltimaAula = aula.getDataHora().toLocalDate();
                LocalDate dataFinal = dataUltimaAula.plusMonths(3); // Gera mais 3 meses

                // Gera aulas semanais até a dataFinal
                for (LocalDate data = dataUltimaAula.plusWeeks(1); !data.isAfter(dataFinal); data = data.plusWeeks(1)) {
                    LocalDateTime novaDataHora = LocalDateTime.of(data, aula.getHorarioAula());

                    if (!aulaRepository.existsByAlunoAndDataHora(aluno, novaDataHora)) {
                        Aula novaAula = new Aula();
                        novaAula.setAluno(aluno);
                        novaAula.setProfessor(aula.getProfessor());
                        novaAula.setDataHora(novaDataHora);
                        novaAula.setDiaSemanaAula(aula.getDiaSemanaAula());
                        novaAula.setHorarioPadrao(aula.getHorarioAula());
                        novaAula.setRecorrente(true);
                        novaAula.setStatus(StatusAula.AGENDADA);

                        aulaRepository.save(novaAula);
                    }
                }
            }
        }
    }


}







