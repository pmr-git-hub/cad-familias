package br.gov.pmr.cad_familias.service.programa;

import br.gov.pmr.cad_familias.domain.audit.AcaoAudit;
import br.gov.pmr.cad_familias.domain.programa.ProgramaSocial;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.dto.programa.ProgramaSocialResponse;
import br.gov.pmr.cad_familias.dto.programa.ProgramaSocialCreateRequest;
import br.gov.pmr.cad_familias.dto.programa.ProgramaSocialUpdateRequest;
import br.gov.pmr.cad_familias.excecao.UsuarioNaoEncontradoException;
import br.gov.pmr.cad_familias.mapper.programa.ProgramaSocialMapper;
import br.gov.pmr.cad_familias.repository.programa.ProgramaSocialRepository;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import br.gov.pmr.cad_familias.service.audit.AuditService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramaSocialService {

    private final ProgramaSocialRepository repository;
    private final ProgramaSocialMapper mapper;
    private final AuditService auditService;
    private final UsuarioRepository usuarioRepository;

    public ProgramaSocialService(ProgramaSocialRepository repository,
                                 ProgramaSocialMapper mapper,
                                 AuditService auditService,
                                 UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.auditService = auditService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ProgramaSocialResponse criar(ProgramaSocialCreateRequest request, Long usuarioId) {
        if (repository.existsByNomeIgnoreCase(request.getNome())) {
            throw new IllegalArgumentException("Já existe um programa social com o nome: " + request.getNome());
        }

        ProgramaSocial entity = mapper.toEntity(request);
        entity.setCriadoPor(usuarioId);

        // ✅ Salva
        ProgramaSocial entitySalva = repository.save(entity);

        // ✅ Converte
        ProgramaSocialResponse resultado = mapper.toResponse(entitySalva);

        // ✅ Auditoria (INSERT)
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "programa_social",
                entitySalva.getId(),
                AcaoAudit.INSERT,
                null,
                resultado,
                usuario
        );

        return resultado;
    }

    @Transactional
    public ProgramaSocialResponse atualizar(Long id, ProgramaSocialUpdateRequest request, Long usuarioId) {
        ProgramaSocial entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Programa social não encontrado: " + id));

        // ✅ Estado ANTES
        ProgramaSocialResponse estadoAnterior = mapper.toResponse(entity);

        // Só valida duplicidade de nome se o nome veio no request
        if (request.getNome() != null && !request.getNome().isBlank()) {
            if (repository.existsByNomeIgnoreCaseAndIdNot(request.getNome(), id)) {
                throw new IllegalArgumentException("Já existe outro programa social com o nome: " + request.getNome());
            }
            entity.setNome(request.getNome());
        }

        if (request.getCriterios() != null) {
            entity.setCriterios(request.getCriterios());
        }

        if (request.getOrgaoGestor() != null) {
            entity.setOrgaoGestor(request.getOrgaoGestor());
        }

        if (request.getAtivo() != null) {
            entity.setAtivo(request.getAtivo());
        }

        entity.setAtualizadoPor(usuarioId);

        // ✅ Salva
        ProgramaSocial entitySalva = repository.save(entity);

        // ✅ Estado DEPOIS
        ProgramaSocialResponse estadoNovo = mapper.toResponse(entitySalva);

        // ✅ Auditoria
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "programa_social",
                entitySalva.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }

    @Transactional(readOnly = true)
    public ProgramaSocialResponse buscarPorId(Long id) {
        ProgramaSocial entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Programa social não encontrado: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<ProgramaSocialResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProgramaSocialResponse> listarAtivos() {
        return repository.findByAtivoTrue()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProgramaSocialResponse> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProgramaSocialResponse desativar(Long id, Long usuarioId) {
        ProgramaSocial entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Programa social não encontrado: " + id));

        // ✅ Estado ANTES
        ProgramaSocialResponse estadoAnterior = mapper.toResponse(entity);

        entity.setAtivo(false);
        entity.setAtualizadoPor(usuarioId);

        // ✅ Salva
        ProgramaSocial entitySalva = repository.save(entity);

        // ✅ Estado DEPOIS
        ProgramaSocialResponse estadoNovo = mapper.toResponse(entitySalva);

        // ✅ Auditoria
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        auditService.registrar(
                "programa_social",
                entitySalva.getId(),
                AcaoAudit.UPDATE,
                estadoAnterior,
                estadoNovo,
                usuario
        );

        return estadoNovo;
    }
}
