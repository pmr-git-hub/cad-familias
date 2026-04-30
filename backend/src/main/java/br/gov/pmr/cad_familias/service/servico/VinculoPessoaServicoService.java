package br.gov.pmr.cad_familias.service.servico;

import br.gov.pmr.cad_familias.domain.audit.AcaoAudit;
import br.gov.pmr.cad_familias.domain.familia.Pessoa;
import br.gov.pmr.cad_familias.domain.programa.StatusVinculo;
import br.gov.pmr.cad_familias.domain.servico.Servico;
import br.gov.pmr.cad_familias.domain.servico.VinculoPessoaServico;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.dto.programa.VinculoDesligamentoRequest;
import br.gov.pmr.cad_familias.dto.servico.VinculoPessoaServicoRequest;
import br.gov.pmr.cad_familias.dto.servico.VinculoPessoaServicoResponse;
import br.gov.pmr.cad_familias.excecao.UsuarioNaoEncontradoException;
import br.gov.pmr.cad_familias.mapper.servico.VinculoPessoaServicoMapper;
import br.gov.pmr.cad_familias.repository.familia.PessoaRepository;
import br.gov.pmr.cad_familias.repository.servico.ServicoRepository;
import br.gov.pmr.cad_familias.repository.servico.VinculoPessoaServicoRepository;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import br.gov.pmr.cad_familias.service.audit.AuditService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VinculoPessoaServicoService {

    private final VinculoPessoaServicoRepository vinculoRepository;
    private final PessoaRepository pessoaRepository;
    private final ServicoRepository servicoRepository;
    private final VinculoPessoaServicoMapper mapper;
    private final AuditService auditService;
    private final UsuarioRepository usuarioRepository;

    public VinculoPessoaServicoService(
            VinculoPessoaServicoRepository vinculoRepository,
            PessoaRepository pessoaRepository,
            ServicoRepository servicoRepository,
            VinculoPessoaServicoMapper mapper,
            AuditService auditService,
            UsuarioRepository usuarioRepository
    ) {
        this.vinculoRepository = vinculoRepository;
        this.pessoaRepository = pessoaRepository;
        this.servicoRepository = servicoRepository;
        this.mapper = mapper;
        this.auditService = auditService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public VinculoPessoaServicoResponse vincular(VinculoPessoaServicoRequest request, Long usuarioId) {
        Pessoa pessoa = pessoaRepository.findById(request.getPessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada: " + request.getPessoaId()));

        Servico servico = servicoRepository.findById(request.getServicoId())
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado: " + request.getServicoId()));

        if (!servico.isAtivo()) {
            throw new IllegalArgumentException("Não é possível vincular a um serviço inativo: " + servico.getNome());
        }

        // Verifica se já existe vínculo ativo
        if (vinculoRepository.existsByPessoaIdAndServicoIdAndStatus(
                request.getPessoaId(), request.getServicoId(), StatusVinculo.ATIVO)) {
            throw new IllegalArgumentException(
                    "Pessoa já possui vínculo ativo com o serviço: " + servico.getNome()
            );
        }

        // Validação de faixa etária (se configurada no serviço)
        if (servico.getFaixaEtariaMin() != null || servico.getFaixaEtariaMax() != null) {
            int idade = pessoa.getIdade();
            if (servico.getFaixaEtariaMin() != null && idade < servico.getFaixaEtariaMin()) {
                throw new IllegalArgumentException(
                        "Pessoa não atende à idade mínima (" + servico.getFaixaEtariaMin() + " anos) do serviço"
                );
            }
            if (servico.getFaixaEtariaMax() != null && idade > servico.getFaixaEtariaMax()) {
                throw new IllegalArgumentException(
                        "Pessoa excede a idade máxima (" + servico.getFaixaEtariaMax() + " anos) do serviço"
                );
            }
        }

        VinculoPessoaServico entity = mapper.toEntity(request, pessoa, servico);
        entity.setCriadoPor(usuarioId);

        // ✅ Salva
        VinculoPessoaServico entitySalva = vinculoRepository.save(entity);

        // ✅ Converte
        VinculoPessoaServicoResponse resultado = mapper.toResponse(entitySalva);

        // ✅ Auditoria (INSERT)
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "vinculo_pessoa_servico",
                entitySalva.getId(),
                AcaoAudit.INSERT,
                null,
                resultado,
                usuario
        );

        return resultado;
    }

    @Transactional
    public VinculoPessoaServicoResponse desligar(Long vinculoId, VinculoDesligamentoRequest request, Long usuarioId) {
        VinculoPessoaServico entity = vinculoRepository.findById(vinculoId)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado: " + vinculoId));

        // ✅ Estado ANTES
        VinculoPessoaServicoResponse estadoAnterior = mapper.toResponse(entity);

        if (entity.getStatus() != StatusVinculo.ATIVO) {
            throw new IllegalArgumentException("Somente vínculos ativos podem ser desligados. Status atual: " + entity.getStatus());
        }

        if (request.getDataSaida().isBefore(entity.getDataEntrada())) {
            throw new IllegalArgumentException("Data de saída não pode ser anterior à data de entrada");
        }

        entity.setDataSaida(request.getDataSaida());
        entity.setMotivoSaida(request.getMotivoSaida());
        entity.setStatus(request.getStatus() != null ? request.getStatus() : StatusVinculo.CANCELADO);
        entity.setAtualizadoPor(usuarioId);

        // ✅ Salva
        VinculoPessoaServico entitySalva = vinculoRepository.save(entity);

        // ✅ Estado DEPOIS
        VinculoPessoaServicoResponse estadoNovo = mapper.toResponse(entitySalva);

        // ✅ Auditoria
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "vinculo_pessoa_servico",
                entitySalva.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }

    @Transactional
    public VinculoPessoaServicoResponse suspender(Long vinculoId, Long usuarioId) {
        VinculoPessoaServico entity = vinculoRepository.findById(vinculoId)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado: " + vinculoId));

        // ✅ Estado ANTES
        VinculoPessoaServicoResponse estadoAnterior = mapper.toResponse(entity);

        if (entity.getStatus() != StatusVinculo.ATIVO) {
            throw new IllegalArgumentException("Somente vínculos ativos podem ser suspensos. Status atual: " + entity.getStatus());
        }

        entity.setStatus(StatusVinculo.SUSPENSO);
        entity.setAtualizadoPor(usuarioId);

        // ✅ Salva
        VinculoPessoaServico entitySalva = vinculoRepository.save(entity);

        // ✅ Estado DEPOIS
        VinculoPessoaServicoResponse estadoNovo = mapper.toResponse(entitySalva);

        // ✅ Auditoria
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "vinculo_pessoa_servico",
                entitySalva.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }

    @Transactional
    public VinculoPessoaServicoResponse reativar(Long vinculoId, Long usuarioId) {
        VinculoPessoaServico entity = vinculoRepository.findById(vinculoId)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado: " + vinculoId));

        // ✅ Estado ANTES
        VinculoPessoaServicoResponse estadoAnterior = mapper.toResponse(entity);

        if (entity.getStatus() != StatusVinculo.SUSPENSO) {
            throw new IllegalArgumentException("Somente vínculos suspensos podem ser reativados. Status atual: " + entity.getStatus());
        }

        entity.setStatus(StatusVinculo.ATIVO);
        entity.setDataSaida(null);
        entity.setMotivoSaida(null);
        entity.setAtualizadoPor(usuarioId);

        // ✅ Salva
        VinculoPessoaServico entitySalva = vinculoRepository.save(entity);

        // ✅ Estado DEPOIS
        VinculoPessoaServicoResponse estadoNovo = mapper.toResponse(entitySalva);

        // ✅ Auditoria
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "vinculo_pessoa_servico",
                entitySalva.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }

    @Transactional(readOnly = true)
    public VinculoPessoaServicoResponse buscarPorId(Long id) {
        VinculoPessoaServico entity = vinculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<VinculoPessoaServicoResponse> listarPorPessoa(Long pessoaId) {
        return vinculoRepository.findByPessoaId(pessoaId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VinculoPessoaServicoResponse> listarAtivosPorPessoa(Long pessoaId) {
        return vinculoRepository.findByPessoaIdAndStatus(pessoaId, StatusVinculo.ATIVO)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VinculoPessoaServicoResponse> listarPorServico(Long servicoId) {
        return vinculoRepository.findByServicoId(servicoId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VinculoPessoaServicoResponse> listarAtivosPorServico(Long servicoId) {
        return vinculoRepository.findByServicoIdAndStatus(servicoId, StatusVinculo.ATIVO)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
