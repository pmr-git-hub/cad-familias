package br.gov.pmr.cad_familias.service.encaminhamento;

import br.gov.pmr.cad_familias.domain.audit.AcaoAudit;
import br.gov.pmr.cad_familias.domain.encaminhamento.Encaminhamento;
import br.gov.pmr.cad_familias.domain.encaminhamento.StatusEncaminhamento;
import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.dto.encaminhamento.EncaminhamentoAtualizacaoStatusDTO;
import br.gov.pmr.cad_familias.dto.encaminhamento.EncaminhamentoCadastroDTO;
import br.gov.pmr.cad_familias.dto.encaminhamento.EncaminhamentoRespostaDTO;
import br.gov.pmr.cad_familias.excecao.UsuarioNaoEncontradoException;
import br.gov.pmr.cad_familias.repository.encaminhamento.EncaminhamentoRepository;
import br.gov.pmr.cad_familias.repository.equipamento.EquipamentoRepository;
import br.gov.pmr.cad_familias.repository.familia.FamiliaRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import br.gov.pmr.cad_familias.service.audit.AuditService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EncaminhamentoService {

    private final EncaminhamentoRepository encaminhamentoRepository;
    private final FamiliaRepository familiaRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final TecnicoRepository tecnicoRepository;
    private final AuditService auditService;
    private final UsuarioRepository usuarioRepository;

    public EncaminhamentoService(EncaminhamentoRepository encaminhamentoRepository,
                                 FamiliaRepository familiaRepository,
                                 EquipamentoRepository equipamentoRepository,
                                 TecnicoRepository tecnicoRepository,
                                 AuditService auditService,
                                 UsuarioRepository usuarioRepository) {
        this.encaminhamentoRepository = encaminhamentoRepository;
        this.familiaRepository = familiaRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.auditService = auditService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public EncaminhamentoRespostaDTO cadastrar(EncaminhamentoCadastroDTO dto, Long usuarioId) {
        Familia familia = familiaRepository.findById(dto.familiaId())
                .orElseThrow(() -> new EntityNotFoundException("Família não encontrada com ID: " + dto.familiaId()));

        Equipamento origem = equipamentoRepository.findById(dto.equipamentoOrigemId())
                .orElseThrow(() -> new EntityNotFoundException("Equipamento de origem não encontrado com ID: " + dto.equipamentoOrigemId()));

        Equipamento destino = equipamentoRepository.findById(dto.equipamentoDestinoId())
                .orElseThrow(() -> new EntityNotFoundException("Equipamento de destino não encontrado com ID: " + dto.equipamentoDestinoId()));

        if (origem.getId().equals(destino.getId())) {
            throw new IllegalArgumentException("O equipamento de origem e destino não podem ser o mesmo.");
        }

        Tecnico tecnico = tecnicoRepository.findById(dto.tecnicoId())
                .orElseThrow(() -> new EntityNotFoundException("Técnico não encontrado com ID: " + dto.tecnicoId()));

        Encaminhamento encaminhamento = new Encaminhamento();
        encaminhamento.setFamilia(familia);
        encaminhamento.setEquipamentoOrigem(origem);
        encaminhamento.setEquipamentoDestino(destino);
        encaminhamento.setTecnico(tecnico);
        encaminhamento.setMotivo(dto.motivo());
        encaminhamento.setCriadoPor(usuarioId);

        // ✅ Salva
        Encaminhamento encaminhamentoSalvo = encaminhamentoRepository.save(encaminhamento);

        // ✅ Converte
        EncaminhamentoRespostaDTO resultado = EncaminhamentoRespostaDTO.fromEntity(encaminhamentoSalvo);

        // ✅ Auditoria (INSERT)
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "encaminhamento",
                encaminhamentoSalvo.getId(),
                AcaoAudit.INSERT,
                null,
                resultado,
                usuario
        );

        return resultado;
    }

    @Transactional(readOnly = true)
    public EncaminhamentoRespostaDTO buscarPorId(Long id) {
        Encaminhamento encaminhamento = encaminhamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Encaminhamento não encontrado com ID: " + id));

        return EncaminhamentoRespostaDTO.fromEntity(encaminhamento);
    }

    @Transactional(readOnly = true)
    public List<EncaminhamentoRespostaDTO> listarPorFamilia(Long familiaId) {
        return encaminhamentoRepository.findByFamiliaIdOrderByDataDesc(familiaId)
                .stream()
                .map(EncaminhamentoRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EncaminhamentoRespostaDTO> listarPendentesDoEquipamento(Long equipamentoId) {
        return encaminhamentoRepository.findByEquipamentoDestinoIdAndStatusOrderByDataDesc(equipamentoId, StatusEncaminhamento.PENDENTE)
                .stream()
                .map(EncaminhamentoRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EncaminhamentoRespostaDTO> listarPorEquipamentoOrigem(Long equipamentoId) {
        return encaminhamentoRepository.findByEquipamentoOrigemIdOrderByDataDesc(equipamentoId)
                .stream()
                .map(EncaminhamentoRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EncaminhamentoRespostaDTO> listarPorTecnico(Long tecnicoId) {
        return encaminhamentoRepository.findByTecnicoIdOrderByDataDesc(tecnicoId)
                .stream()
                .map(EncaminhamentoRespostaDTO::fromEntity)
                .toList();
    }

    @Transactional
    public EncaminhamentoRespostaDTO atualizarStatus(Long id, EncaminhamentoAtualizacaoStatusDTO dto, Long usuarioId) {
        Encaminhamento encaminhamento = encaminhamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Encaminhamento não encontrado com ID: " + id));

        // ✅ Estado ANTES
        EncaminhamentoRespostaDTO estadoAnterior = EncaminhamentoRespostaDTO.fromEntity(encaminhamento);

        validarTransicaoStatus(encaminhamento.getStatus(), dto.status());

        encaminhamento.setStatus(dto.status());
        encaminhamento.setAtualizadoPor(usuarioId);

        // ✅ Salva
        Encaminhamento encaminhamentoSalvo = encaminhamentoRepository.save(encaminhamento);

        // ✅ Estado DEPOIS
        EncaminhamentoRespostaDTO estadoNovo = EncaminhamentoRespostaDTO.fromEntity(encaminhamentoSalvo);

        // ✅ Auditoria
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "encaminhamento",
                encaminhamentoSalvo.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }

    private void validarTransicaoStatus(StatusEncaminhamento atual, StatusEncaminhamento novo) {
        switch (atual) {
            case PENDENTE -> {
                if (novo != StatusEncaminhamento.ACEITO && novo != StatusEncaminhamento.RECUSADO) {
                    throw new IllegalArgumentException("Encaminhamento PENDENTE só pode ser ACEITO ou RECUSADO.");
                }
            }
            case ACEITO -> {
                if (novo != StatusEncaminhamento.CONCLUIDO) {
                    throw new IllegalArgumentException("Encaminhamento ACEITO só pode ser CONCLUÍDO.");
                }
            }
            case RECUSADO -> throw new IllegalArgumentException("Encaminhamento RECUSADO não pode mudar de status.");
            case CONCLUIDO -> throw new IllegalArgumentException("Encaminhamento CONCLUÍDO não pode mudar de status.");
        }
    }
}
