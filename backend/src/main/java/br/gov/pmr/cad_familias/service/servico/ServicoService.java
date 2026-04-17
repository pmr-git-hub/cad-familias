package br.gov.pmr.cad_familias.service.servico;

import br.gov.pmr.cad_familias.domain.servico.Servico;
import br.gov.pmr.cad_familias.dto.servico.ServicoCreateRequest;
import br.gov.pmr.cad_familias.dto.servico.ServicoResponse;
import br.gov.pmr.cad_familias.dto.servico.ServicoUpdateRequest;
import br.gov.pmr.cad_familias.mapper.servico.ServicoMapper;
import br.gov.pmr.cad_familias.repository.servico.ServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoService {

    private final ServicoRepository repository;
    private final ServicoMapper mapper;

    public ServicoService(ServicoRepository repository, ServicoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ServicoResponse criar(ServicoCreateRequest request, Long usuarioId) {
        // Validação: nome único por equipamento
        if (repository.existsByNomeIgnoreCaseAndEquipamentoId(request.getNome(), request.getEquipamentoId())) {
            throw new IllegalArgumentException(
                    "Já existe um serviço com o nome '" + request.getNome() + "' neste equipamento");
        }

        // Validação: faixa etária coerente
        validarFaixaEtaria(request.getFaixaEtariaMin(), request.getFaixaEtariaMax());

        Servico entity = mapper.toEntity(request);
        entity.setCriadoPor(usuarioId);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public ServicoResponse atualizar(Long id, ServicoUpdateRequest request, Long usuarioId) {
        Servico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado: " + id));

        // Validação de nome duplicado (só se nome ou equipamento mudaram)
        String nomeCheck = request.getNome() != null && !request.getNome().isBlank()
                ? request.getNome() : entity.getNome();
        Long equipamentoCheck = request.getEquipamentoId() != null
                ? request.getEquipamentoId() : entity.getEquipamentoId();

        if (repository.existsByNomeIgnoreCaseAndEquipamentoIdAndIdNot(nomeCheck, equipamentoCheck, id)) {
            throw new IllegalArgumentException(
                    "Já existe outro serviço com o nome '" + nomeCheck + "' neste equipamento");
        }

        // Validação: faixa etária coerente (considerando merge)
        Integer minCheck = request.getFaixaEtariaMin() != null
                ? request.getFaixaEtariaMin() : entity.getFaixaEtariaMin();
        Integer maxCheck = request.getFaixaEtariaMax() != null
                ? request.getFaixaEtariaMax() : entity.getFaixaEtariaMax();
        validarFaixaEtaria(minCheck, maxCheck);

        mapper.updateEntity(entity, request);
        entity.setAtualizadoPor(usuarioId);

        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public ServicoResponse buscarPorId(Long id) {
        Servico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<ServicoResponse> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicoResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicoResponse> listarPorEquipamento(Long equipamentoId) {
        return repository.findByEquipamentoId(equipamentoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicoResponse> listarAtivosPorEquipamento(Long equipamentoId) {
        return repository.findByEquipamentoIdAndAtivoTrue(equipamentoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicoResponse> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServicoResponse desativar(Long id, Long usuarioId) {
        Servico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado: " + id));

        entity.setAtivo(false);
        entity.setAtualizadoPor(usuarioId);

        return mapper.toResponse(repository.save(entity));
    }

    private void validarFaixaEtaria(Integer min, Integer max) {
        if (min != null && max != null && min > max) {
            throw new IllegalArgumentException(
                    "Faixa etária mínima (" + min + ") não pode ser maior que a máxima (" + max + ")");
        }
    }
}
