package br.gov.pmr.cad_familias.service.atendimento;

import br.gov.pmr.cad_familias.domain.atendimento.Prontuario;
import br.gov.pmr.cad_familias.domain.atendimento.StatusProntuario;
import br.gov.pmr.cad_familias.domain.audit.AcaoAudit;
import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.dto.atendimento.*;
import br.gov.pmr.cad_familias.excecao.TecnicoNaoEncontradoException;
import br.gov.pmr.cad_familias.excecao.UsuarioNaoEncontradoException;
import br.gov.pmr.cad_familias.repository.atendimento.ProntuarioRepository;
import br.gov.pmr.cad_familias.repository.equipamento.EquipamentoRepository;
import br.gov.pmr.cad_familias.repository.familia.FamiliaRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import br.gov.pmr.cad_familias.service.audit.AuditService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final FamiliaRepository familiaRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final TecnicoRepository tecnicoRepository;
    private final AuditService auditService;
    private final UsuarioRepository usuarioRepository;

    public ProntuarioService(ProntuarioRepository prontuarioRepository,
                             FamiliaRepository familiaRepository,
                             EquipamentoRepository equipamentoRepository,
                             TecnicoRepository tecnicoRepository,
                             AuditService auditService,
                             UsuarioRepository usuarioRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.familiaRepository = familiaRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.auditService = auditService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ProntuarioRespostaDTO cadastrar(ProntuarioCadastroDTO dto, Long usuarioId) {

        boolean jaExiste = prontuarioRepository
                .existsByFamiliaIdAndEquipamentoIdAndStatus(dto.familiaId(), dto.equipamentoId(), StatusProntuario.ABERTO);

        if (jaExiste) {
            throw new IllegalStateException("Já existe um prontuário aberto para esta família neste equipamento.");
        }

        Familia familia = familiaRepository.findById(dto.familiaId())
                .orElseThrow(() -> new EntityNotFoundException("Família não encontrada."));

        Equipamento equipamento = equipamentoRepository.findById(dto.equipamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        Tecnico tecnico = tecnicoRepository.findById(usuario.getTecnico().getId())
                .orElseThrow(() -> new TecnicoNaoEncontradoException());

        Prontuario prontuario = new Prontuario();
        prontuario.setFamilia(familia);
        prontuario.setEquipamento(equipamento);
        prontuario.setTecnico(tecnico);
        prontuario.setDataAbertura(LocalDate.now());
        prontuario.setCriadoPor(usuarioId);

        Prontuario prontuarioSalvo = prontuarioRepository.save(prontuario);

        ProntuarioRespostaDTO resultado = ProntuarioRespostaDTO.fromEntity(prontuarioSalvo);


        auditService.registrar(
                "prontuario",
                prontuarioSalvo.getId(),
                AcaoAudit.INSERT,
                null,
                resultado,
                usuario
        );

        return resultado;
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
    public ProntuarioRespostaDTO trocarResponsavel(Long id, ProntuarioResponsavelDTO dto, Long usuarioId) {
        Prontuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado."));

        // ✅ Estado ANTES
        ProntuarioRespostaDTO estadoAnterior = ProntuarioRespostaDTO.fromEntity(prontuario);

        Tecnico tecnico = tecnicoRepository.findById(dto.tecnicoId())
                .orElseThrow(() -> new EntityNotFoundException("Técnico não encontrado."));

        prontuario.setTecnico(tecnico);
        prontuario.setAtualizadoPor(usuarioId);

        // ✅ Salva
        Prontuario prontuarioSalvo = prontuarioRepository.save(prontuario);

        // ✅ Estado DEPOIS
        ProntuarioRespostaDTO estadoNovo = ProntuarioRespostaDTO.fromEntity(prontuarioSalvo);

        // ✅ Auditoria
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "prontuario",
                prontuarioSalvo.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }

    @Transactional
    public ProntuarioRespostaDTO encerrar(Long id, ProntuarioEncerramentoDTO dto, Long usuarioId) {
        Prontuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado."));

        // ✅ Captura estado ANTES
        ProntuarioRespostaDTO estadoAnterior = ProntuarioRespostaDTO.fromEntity(prontuario);

        if (prontuario.getStatus() == StatusProntuario.ENCERRADO) {
            throw new IllegalStateException("Prontuário já está encerrado.");
        }

        prontuario.setStatus(StatusProntuario.ENCERRADO);
        prontuario.setDataFechamento(dto.dataFechamento());
        prontuario.setMotivoEncerramento(dto.motivoEncerramento());
        prontuario.setAtualizadoPor(usuarioId);

        Prontuario prontuarioSalvo = prontuarioRepository.save(prontuario);

        ProntuarioRespostaDTO estadoNovo = ProntuarioRespostaDTO.fromEntity(prontuarioSalvo);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "prontuario",
                prontuarioSalvo.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }


    @Transactional
    public ProntuarioRespostaDTO suspender(Long id, Long usuarioId) {
        Prontuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado."));

        // ✅ Estado ANTES
        ProntuarioRespostaDTO estadoAnterior = ProntuarioRespostaDTO.fromEntity(prontuario);

        if (prontuario.getStatus() != StatusProntuario.ABERTO) {
            throw new IllegalStateException("Apenas prontuários abertos podem ser suspensos.");
        }

        prontuario.setStatus(StatusProntuario.SUSPENSO);
        prontuario.setAtualizadoPor(usuarioId);

        Prontuario prontuarioSalvo = prontuarioRepository.save(prontuario);

        ProntuarioRespostaDTO estadoNovo = ProntuarioRespostaDTO.fromEntity(prontuarioSalvo);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "prontuario",
                prontuarioSalvo.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }

    @Transactional
    public ProntuarioRespostaDTO reabrir(Long id, Long usuarioId) {
        Prontuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado."));

        // ✅ Estado ANTES
        ProntuarioRespostaDTO estadoAnterior = ProntuarioRespostaDTO.fromEntity(prontuario);

        if (prontuario.getStatus() != StatusProntuario.SUSPENSO) {
            throw new IllegalStateException("Apenas prontuários suspensos podem ser reabertos.");
        }

        prontuario.setStatus(StatusProntuario.ABERTO);
        prontuario.setAtualizadoPor(usuarioId);

        Prontuario prontuarioSalvo = prontuarioRepository.save(prontuario);

        ProntuarioRespostaDTO estadoNovo = ProntuarioRespostaDTO.fromEntity(prontuarioSalvo);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "prontuario",
                prontuarioSalvo.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }

}
