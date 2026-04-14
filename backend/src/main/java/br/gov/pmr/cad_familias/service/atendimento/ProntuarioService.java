package br.gov.pmr.cad_familias.service.atendimento;

import br.gov.pmr.cad_familias.domain.atendimento.Prontuario;
import br.gov.pmr.cad_familias.domain.atendimento.StatusProntuario;
import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.dto.atendimento.ProntuarioAtualizacaoDTO;
import br.gov.pmr.cad_familias.dto.atendimento.ProntuarioCadastroDTO;
import br.gov.pmr.cad_familias.dto.atendimento.ProntuarioRespostaDTO;
import br.gov.pmr.cad_familias.repository.atendimento.ProntuarioRepository;
import br.gov.pmr.cad_familias.repository.equipamento.EquipamentoRepository;
import br.gov.pmr.cad_familias.repository.familia.FamiliaRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final FamiliaRepository familiaRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final TecnicoRepository tecnicoRepository;

    public ProntuarioService(ProntuarioRepository prontuarioRepository,
                             FamiliaRepository familiaRepository,
                             EquipamentoRepository equipamentoRepository,
                             TecnicoRepository tecnicoRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.familiaRepository = familiaRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @Transactional
    public ProntuarioRespostaDTO cadastrar(ProntuarioCadastroDTO dto, Long usuarioId) {

        // Verifica se já existe prontuário ABERTO para essa família nesse equipamento
        boolean jaExiste = prontuarioRepository
                .existsByFamiliaIdAndEquipamentoIdAndStatus(dto.familiaId(), dto.equipamentoId(), StatusProntuario.ABERTO);

        if (jaExiste) {
            throw new IllegalStateException("Já existe um prontuário aberto para esta família neste equipamento.");
        }

        Familia familia = familiaRepository.findById(dto.familiaId())
                .orElseThrow(() -> new EntityNotFoundException("Família não encontrada."));

        Equipamento equipamento = equipamentoRepository.findById(dto.equipamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));

        Tecnico tecnico = tecnicoRepository.findById(dto.tecnicoId())
                .orElseThrow(() -> new EntityNotFoundException("Técnico não encontrado."));

        Prontuario prontuario = new Prontuario();
        prontuario.setFamilia(familia);
        prontuario.setEquipamento(equipamento);
        prontuario.setTecnico(tecnico);
        prontuario.setDataAbertura(dto.dataAbertura());
        prontuario.setCriadoPor(usuarioId);

        prontuarioRepository.save(prontuario);

        return ProntuarioRespostaDTO.fromEntity(prontuario);
    }

    @Transactional(readOnly = true)
    public ProntuarioRespostaDTO buscarPorId(Long id) {
        Prontuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado."));
        return ProntuarioRespostaDTO.fromEntity(prontuario);
    }

    @Transactional(readOnly = true)
    public List<ProntuarioRespostaDTO> listarPorFamilia(Long familiaId) {
        return prontuarioRepository.findByFamiliaId(familiaId).stream()
                .map(ProntuarioRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProntuarioRespostaDTO> listarPorEquipamento(Long equipamentoId) {
        return prontuarioRepository.findByEquipamentoId(equipamentoId).stream()
                .map(ProntuarioRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProntuarioRespostaDTO> listarPorTecnico(Long tecnicoId) {
        return prontuarioRepository.findByTecnicoId(tecnicoId).stream()
                .map(ProntuarioRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional
    public ProntuarioRespostaDTO atualizar(Long id, ProntuarioAtualizacaoDTO dto, Long usuarioId) {
        Prontuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado."));

        Tecnico tecnico = tecnicoRepository.findById(dto.tecnicoId())
                .orElseThrow(() -> new EntityNotFoundException("Técnico não encontrado."));

        prontuario.setTecnico(tecnico);
        prontuario.setStatus(dto.status());
        prontuario.setDataFechamento(dto.dataFechamento());
        prontuario.setAtualizadoPor(usuarioId);

        // Se status for ENCERRADO e não informou data de fechamento, usa hoje
        if (dto.status() == StatusProntuario.ENCERRADO && prontuario.getDataFechamento() == null) {
            prontuario.setDataFechamento(java.time.LocalDate.now());
        }

        prontuarioRepository.save(prontuario);

        return ProntuarioRespostaDTO.fromEntity(prontuario);
    }
}
