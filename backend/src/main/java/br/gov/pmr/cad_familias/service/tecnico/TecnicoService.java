package br.gov.pmr.cad_familias.service.tecnico;

import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.domain.tecnico.TecnicoEquipamento;
import br.gov.pmr.cad_familias.dto.tecnico.TecnicoDTO;
import br.gov.pmr.cad_familias.dto.tecnico.VincularEquipamentoDTO;
import br.gov.pmr.cad_familias.excecao.EquipamentoNaoEncontradoException;
import br.gov.pmr.cad_familias.excecao.TecnicoNaoEncontradoException;
import br.gov.pmr.cad_familias.mapper.tecnico.TecnicoMapper;
import br.gov.pmr.cad_familias.repository.equipamento.EquipamentoRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoEquipamentoRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final TecnicoEquipamentoRepository tecnicoEquipamentoRepository;
    private final EquipamentoRepository equipamentoRepository;

    public TecnicoService(
            TecnicoRepository tecnicoRepository,
            TecnicoEquipamentoRepository tecnicoEquipamentoRepository,
            EquipamentoRepository equipamentoRepository
    ) {
        this.tecnicoRepository = tecnicoRepository;
        this.tecnicoEquipamentoRepository = tecnicoEquipamentoRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    public List<TecnicoDTO> listarTecnicos() {
        List<Tecnico> tecnicos = tecnicoRepository.findAll();
        return tecnicos.stream().map(t -> {
            List<TecnicoEquipamento> vinculos =
                    tecnicoEquipamentoRepository.findByTecnicoIdAndAtivoTrue(t.getId());
            return TecnicoMapper.tecnicoToDTO(t, vinculos);
        }).toList();
    }

    public TecnicoDTO buscarPorId(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(TecnicoNaoEncontradoException::new);

        List<TecnicoEquipamento> vinculos =
                tecnicoEquipamentoRepository.findByTecnicoIdAndAtivoTrue(tecnico.getId());

        return TecnicoMapper.tecnicoToDTO(tecnico, vinculos);
    }

    @Transactional
    public TecnicoDTO criarTecnico(TecnicoDTO dto, Long usuarioId) {
        Tecnico tecnico = TecnicoMapper.dtoToTecnico(dto);
        tecnico.setCriadoPor(usuarioId);
        tecnico.setAtualizadoPor(usuarioId);
        tecnico = tecnicoRepository.save(tecnico);

        return TecnicoMapper.tecnicoToDTO(tecnico);
    }

    @Transactional
    public TecnicoDTO atualizarTecnico(Long id, TecnicoDTO dto, Long usuarioId) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(TecnicoNaoEncontradoException::new);

        if (dto.getNome() != null) {
            tecnico.setNome(dto.getNome());
        }
        if (dto.getCpf() != null) {
            tecnico.setCpf(dto.getCpf());
        }
        if (dto.getRegistroProfissional() != null) {
            tecnico.setRegistroProfissional(dto.getRegistroProfissional());
        }
        if (dto.getEspecialidade() != null) {
            tecnico.setEspecialidade(dto.getEspecialidade());
        }
        if (dto.isAtivo() != tecnico.isAtivo()) {
            tecnico.setAtivo(dto.isAtivo());
        }

        tecnico.setAtualizadoPor(usuarioId);
        tecnico = tecnicoRepository.save(tecnico);

        List<TecnicoEquipamento> vinculos =
                tecnicoEquipamentoRepository.findByTecnicoIdAndAtivoTrue(tecnico.getId());

        return TecnicoMapper.tecnicoToDTO(tecnico, vinculos);
    }

    @Transactional
    public void desativar(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(TecnicoNaoEncontradoException::new);
        tecnico.setAtivo(false);
        tecnicoRepository.save(tecnico);
    }

    // ─── Vínculos com Equipamento ───────────────────────────

    @Transactional
    public TecnicoDTO vincularEquipamento(Long tecnicoId, VincularEquipamentoDTO dto, Long usuarioId) {
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(TecnicoNaoEncontradoException::new);

        Equipamento equipamento = equipamentoRepository.findById(dto.getEquipamentoId())
                .orElseThrow(EquipamentoNaoEncontradoException::new);
        boolean jaVinculado = tecnicoEquipamentoRepository
                .existsByTecnicoIdAndEquipamentoIdAndAtivoTrue(tecnicoId, dto.getEquipamentoId());


        if (jaVinculado) {
            throw new IllegalStateException("Técnico já está vinculado a este equipamento.");
        }

        TecnicoEquipamento vinculo = new TecnicoEquipamento();
        vinculo.setTecnico(tecnico);
        vinculo.setEquipamento(equipamento);
        vinculo.setDataInicio(dto.getDataInicio());
        vinculo.setAtivo(true);
        vinculo.setCriadoPor(usuarioId);
        tecnicoEquipamentoRepository.save(vinculo);

        List<TecnicoEquipamento> vinculos =
                tecnicoEquipamentoRepository.findByTecnicoIdAndAtivoTrue(tecnicoId);

        return TecnicoMapper.tecnicoToDTO(tecnico, vinculos);
    }

    @Transactional
    public TecnicoDTO desvincularEquipamento(Long tecnicoId, Long equipamentoId) {
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(TecnicoNaoEncontradoException::new);

        List<TecnicoEquipamento> vinculos =
                tecnicoEquipamentoRepository.findByTecnicoIdAndAtivoTrue(tecnicoId);

        TecnicoEquipamento vinculo = vinculos.stream()
                .filter(v -> v.getEquipamento().getId().equals(equipamentoId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Vínculo não encontrado."));

        vinculo.setAtivo(false);
        vinculo.setDataFim(LocalDate.now());
        tecnicoEquipamentoRepository.save(vinculo);

        List<TecnicoEquipamento> vinculosAtualizados =
                tecnicoEquipamentoRepository.findByTecnicoIdAndAtivoTrue(tecnicoId);

        return TecnicoMapper.tecnicoToDTO(tecnico, vinculosAtualizados);
    }
}
