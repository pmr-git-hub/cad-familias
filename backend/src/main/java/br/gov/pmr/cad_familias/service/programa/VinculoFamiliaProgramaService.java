package br.gov.pmr.cad_familias.service.programa;

import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.domain.programa.ProgramaSocial;
import br.gov.pmr.cad_familias.domain.programa.StatusVinculo;
import br.gov.pmr.cad_familias.domain.programa.VinculoFamiliaPrograma;
import br.gov.pmr.cad_familias.dto.programa.VinculoDesligamentoRequest;
import br.gov.pmr.cad_familias.dto.programa.VinculoFamiliaProgramaRequest;
import br.gov.pmr.cad_familias.dto.programa.VinculoFamiliaProgramaResponse;
import br.gov.pmr.cad_familias.mapper.programa.VinculoFamiliaProgramaMapper;
import br.gov.pmr.cad_familias.repository.familia.FamiliaRepository;
import br.gov.pmr.cad_familias.repository.programa.ProgramaSocialRepository;
import br.gov.pmr.cad_familias.repository.programa.VinculoFamiliaProgramaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VinculoFamiliaProgramaService {

    private final VinculoFamiliaProgramaRepository vinculoRepository;
    private final FamiliaRepository familiaRepository;
    private final ProgramaSocialRepository programaRepository;
    private final VinculoFamiliaProgramaMapper mapper;

    public VinculoFamiliaProgramaService(
            VinculoFamiliaProgramaRepository vinculoRepository,
            FamiliaRepository familiaRepository,
            ProgramaSocialRepository programaRepository,
            VinculoFamiliaProgramaMapper mapper
    ) {
        this.vinculoRepository = vinculoRepository;
        this.familiaRepository = familiaRepository;
        this.programaRepository = programaRepository;
        this.mapper = mapper;
    }

    @Transactional
    public VinculoFamiliaProgramaResponse vincular(VinculoFamiliaProgramaRequest request, Long usuarioId) {
        Familia familia = familiaRepository.findById(request.getFamiliaId())
                .orElseThrow(() -> new EntityNotFoundException("Família não encontrada: " + request.getFamiliaId()));

        ProgramaSocial programa = programaRepository.findById(request.getProgramaId())
                .orElseThrow(() -> new EntityNotFoundException("Programa social não encontrado: " + request.getProgramaId()));

        if (!programa.isAtivo()) {
            throw new IllegalArgumentException("Não é possível vincular a um programa inativo: " + programa.getNome());
        }

        // Verifica se já existe vínculo ativo para esta família neste programa
        if (vinculoRepository.existsByFamiliaIdAndProgramaIdAndStatus(
                request.getFamiliaId(), request.getProgramaId(), StatusVinculo.ATIVO)) {
            throw new IllegalArgumentException(
                    "Família já possui vínculo ativo com o programa: " + programa.getNome()
            );
        }

        VinculoFamiliaPrograma entity = mapper.toEntity(request, familia, programa);
        entity.setCriadoPor(usuarioId);

        return mapper.toResponse(vinculoRepository.save(entity));
    }

    @Transactional
    public VinculoFamiliaProgramaResponse desligar(Long vinculoId, VinculoDesligamentoRequest request, Long usuarioId) {
        VinculoFamiliaPrograma entity = vinculoRepository.findById(vinculoId)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado: " + vinculoId));

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

        return mapper.toResponse(vinculoRepository.save(entity));
    }

    @Transactional
    public VinculoFamiliaProgramaResponse suspender(Long vinculoId, Long usuarioId) {
        VinculoFamiliaPrograma entity = vinculoRepository.findById(vinculoId)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado: " + vinculoId));

        if (entity.getStatus() != StatusVinculo.ATIVO) {
            throw new IllegalArgumentException("Somente vínculos ativos podem ser suspensos. Status atual: " + entity.getStatus());
        }

        entity.setStatus(StatusVinculo.SUSPENSO);
        entity.setAtualizadoPor(usuarioId);

        return mapper.toResponse(vinculoRepository.save(entity));
    }

    @Transactional
    public VinculoFamiliaProgramaResponse reativar(Long vinculoId, Long usuarioId) {
        VinculoFamiliaPrograma entity = vinculoRepository.findById(vinculoId)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado: " + vinculoId));

        if (entity.getStatus() != StatusVinculo.SUSPENSO) {
            throw new IllegalArgumentException("Somente vínculos suspensos podem ser reativados. Status atual: " + entity.getStatus());
        }

        entity.setStatus(StatusVinculo.ATIVO);
        entity.setDataSaida(null);
        entity.setMotivoSaida(null);
        entity.setAtualizadoPor(usuarioId);

        return mapper.toResponse(vinculoRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public VinculoFamiliaProgramaResponse buscarPorId(Long id) {
        VinculoFamiliaPrograma entity = vinculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado: " + id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<VinculoFamiliaProgramaResponse> listarPorFamilia(Long familiaId) {
        return vinculoRepository.findByFamiliaId(familiaId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VinculoFamiliaProgramaResponse> listarAtivosPorFamilia(Long familiaId) {
        return vinculoRepository.findByFamiliaIdAndStatus(familiaId, StatusVinculo.ATIVO)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VinculoFamiliaProgramaResponse> listarPorPrograma(Long programaId) {
        return vinculoRepository.findByProgramaId(programaId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VinculoFamiliaProgramaResponse> listarAtivosPorPrograma(Long programaId) {
        return vinculoRepository.findByProgramaIdAndStatus(programaId, StatusVinculo.ATIVO)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
