package br.gov.pmr.cad_familias.service.programa;

import br.gov.pmr.cad_familias.domain.programa.ProgramaSocial;
import br.gov.pmr.cad_familias.dto.programa.ProgramaSocialResponse;
import br.gov.pmr.cad_familias.dto.programa.ProgramaSocialRequest;
import br.gov.pmr.cad_familias.mapper.programa.ProgramaSocialMapper;
import br.gov.pmr.cad_familias.repository.programa.ProgramaSocialRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramaSocialService {

    private final ProgramaSocialRepository repository;
    private final ProgramaSocialMapper mapper;

    public ProgramaSocialService(ProgramaSocialRepository repository, ProgramaSocialMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ProgramaSocialResponse criar(ProgramaSocialRequest request, Long usuarioId) {
        if (repository.existsByNomeIgnoreCase(request.getNome())) {
            throw new IllegalArgumentException("Já existe um programa social com o nome: " + request.getNome());
        }

        ProgramaSocial entity = mapper.toEntity(request);
        entity.setCriadoPor(usuarioId);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ProgramaSocialResponse atualizar(Long id, ProgramaSocialRequest request, Long usuarioId) {
        ProgramaSocial entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Programa social não encontrado: " + id));

        if (repository.existsByNomeIgnoreCaseAndIdNot(request.getNome(), id)) {
            throw new IllegalArgumentException("Já existe outro programa social com o nome: " + request.getNome());
        }

        mapper.updateEntity(entity, request);
        entity.setAtualizadoPor(usuarioId);

        return mapper.toResponse(repository.save(entity));
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

        entity.setAtivo(false);
        entity.setAtualizadoPor(usuarioId);

        return mapper.toResponse(repository.save(entity));
    }
}
