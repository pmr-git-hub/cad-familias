package br.gov.pmr.cad_familias.service.gestacao;

import br.gov.pmr.cad_familias.domain.audit.AcaoAudit;
import br.gov.pmr.cad_familias.domain.gestacao.Gestacao;
import br.gov.pmr.cad_familias.domain.gestacao.GestacaoAcompanhamento;
import br.gov.pmr.cad_familias.domain.gestacao.StatusGestacao;
import br.gov.pmr.cad_familias.domain.servico.VinculoPessoaServico;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.dto.gestacao.*;
import br.gov.pmr.cad_familias.excecao.UsuarioNaoEncontradoException;
import br.gov.pmr.cad_familias.repository.gestacao.GestacaoAcompanhamentoRepository;
import br.gov.pmr.cad_familias.repository.gestacao.GestacaoRepository;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import br.gov.pmr.cad_familias.service.audit.AuditService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestacaoService {

    private final GestacaoRepository gestacaoRepository;
    private final GestacaoAcompanhamentoRepository acompanhamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditService auditService;

    public GestacaoService(
            GestacaoRepository gestacaoRepository,
            GestacaoAcompanhamentoRepository acompanhamentoRepository,
            UsuarioRepository usuarioRepository,
            AuditService auditService
    ) {
        this.gestacaoRepository = gestacaoRepository;
        this.acompanhamentoRepository = acompanhamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditService = auditService;
    }

    // ─── Criação automática via vínculo ──────────────────────────────────────────

    @Transactional
    public Gestacao criarAutomaticamente(VinculoPessoaServico vinculo, Long usuarioId) {
        Gestacao g = new Gestacao();
        g.setVinculo(vinculo);
        g.setStatus(StatusGestacao.EM_ACOMPANHAMENTO);
        g.setCriadoPor(usuarioId);

        Gestacao salva = gestacaoRepository.save(g);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        auditService.registrar(
                "gestacao",
                salva.getId(),
                AcaoAudit.INSERT,
                null,
                GestacaoRespostaDTO.fromEntity(salva),
                usuario
        );

        return salva;
    }

    // ─── Consultas ───────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public GestacaoRespostaDTO buscarPorId(Long id) {
        return GestacaoRespostaDTO.fromEntity(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public GestacaoRespostaDTO buscarPorVinculo(Long vinculoId) {
        Gestacao g = gestacaoRepository.findByVinculoId(vinculoId)
                .orElseThrow(() -> new EntityNotFoundException("Gestação não encontrada para o vínculo: " + vinculoId));
        return GestacaoRespostaDTO.fromEntity(g);
    }

    // ─── Atualização ─────────────────────────────────────────────────────────────

    @Transactional
    public GestacaoRespostaDTO atualizar(Long id, GestacaoAtualizacaoDTO dto, Long usuarioId) {
        Gestacao g = findOrThrow(id);
        GestacaoRespostaDTO estadoAnterior = GestacaoRespostaDTO.fromEntity(g);

        if (dto.dataUltimaMenstruacao() != null) g.setDataUltimaMenstruacao(dto.dataUltimaMenstruacao());
        if (dto.dataPrevistaParto() != null) g.setDataPrevistaParto(dto.dataPrevistaParto());
        if (dto.status() != null) g.setStatus(dto.status());
        g.setAtualizadoPor(usuarioId);
        g.setAtualizadoEm(LocalDateTime.now());

        Gestacao salva = gestacaoRepository.save(g);
        GestacaoRespostaDTO estadoNovo = GestacaoRespostaDTO.fromEntity(salva);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        auditService.registrar("gestacao", salva.getId(), AcaoAudit.UPDATE, estadoAnterior, estadoNovo, usuario);

        return estadoNovo;
    }

    // ─── Acompanhamentos ─────────────────────────────────────────────────────────

    @Transactional
    public GestacaoAcompanhamentoRespostaDTO registrarAcompanhamento(
            Long gestacaoId, GestacaoAcompanhamentoCadastroDTO dto, Long usuarioId) {

        Gestacao g = findOrThrow(gestacaoId);

        if (g.getStatus() != StatusGestacao.EM_ACOMPANHAMENTO) {
            throw new IllegalArgumentException(
                    "Não é possível registrar acompanhamento para gestação com status: " + g.getStatus()
            );
        }

        GestacaoAcompanhamento a = new GestacaoAcompanhamento();
        a.setGestacao(g);
        a.setDataRegistro(dto.dataRegistro());
        a.setSemanasGestacao(dto.semanasGestacao());
        a.setNumeroConsultas(dto.numeroConsultas());
        a.setAltoRisco(dto.altoRisco());
        a.setMotivoAltoRisco(dto.motivoAltoRisco());
        a.setPesoKg(dto.pesoKg());
        a.setPressaoArterial(dto.pressaoArterial());
        a.setObservacoes(dto.observacoes());
        a.setCriadoPor(usuarioId);

        GestacaoAcompanhamento salvo = acompanhamentoRepository.save(a);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        GestacaoAcompanhamentoRespostaDTO resultado = GestacaoAcompanhamentoRespostaDTO.fromEntity(salvo);

        auditService.registrar(
                "gestacao_acompanhamento",
                salvo.getId(),
                AcaoAudit.INSERT,
                null,
                resultado,
                usuario
        );

        return resultado;
    }

    @Transactional(readOnly = true)
    public List<GestacaoAcompanhamentoRespostaDTO> listarAcompanhamentos(Long gestacaoId) {
        findOrThrow(gestacaoId); // valida existência
        return acompanhamentoRepository.findByGestacaoIdOrderByDataRegistroDesc(gestacaoId)
                .stream()
                .map(GestacaoAcompanhamentoRespostaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GestacaoAcompanhamentoRespostaDTO buscarAcompanhamento(Long gestacaoId, Long acompanhamentoId) {
        GestacaoAcompanhamento a = acompanhamentoRepository.findByIdAndGestacaoId(acompanhamentoId, gestacaoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Acompanhamento " + acompanhamentoId + " não encontrado para gestação " + gestacaoId));
        return GestacaoAcompanhamentoRespostaDTO.fromEntity(a);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    private Gestacao findOrThrow(Long id) {
        return gestacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gestação não encontrada: " + id));
    }
}
