package br.gov.pmr.cad_familias.service.tecnico;

import br.gov.pmr.cad_familias.dto.tecnico.TecnicoDTO;
import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.excecao.TecnicoNaoEncontradoException;
import br.gov.pmr.cad_familias.mapper.tecnico.TecnicoMapper;
import br.gov.pmr.cad_familias.repository.equipamento.EquipamentoRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final EquipamentoRepository equipamentoRepository;

    public TecnicoService(TecnicoRepository tecnicoRepository,
                          EquipamentoRepository equipamentoRepository) {
        this.tecnicoRepository = tecnicoRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    public List<TecnicoDTO> listarTecnicos() {
        return TecnicoMapper.listaTecnicosToVO(tecnicoRepository.findAll());
    }

    public TecnicoDTO buscarPorId(Long id) {
        return tecnicoRepository.findById(id)
                .map(TecnicoMapper::tecnicoToTecnicoVO)
                .orElseThrow(TecnicoNaoEncontradoException::new);
    }

    @Transactional
    public TecnicoDTO criarTecnico(TecnicoDTO tecnicoDTO, Long usuarioId) {
        Equipamento equipamento = equipamentoRepository.findById(tecnicoDTO.getEquipamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado."));

        Tecnico tecnico = TecnicoMapper.tecnicoVOToTecnico(tecnicoDTO, equipamento);
        tecnico.setCriadoEm(LocalDateTime.now());
        tecnico.setAtualizadoEm(LocalDateTime.now());
        tecnico.setCriadoPor(usuarioId);
        tecnico.setAtualizadoPor(usuarioId);
        return TecnicoMapper.tecnicoToTecnicoVO(tecnicoRepository.save(tecnico));
    }

    @Transactional
    public TecnicoDTO atualizarTecnico(Long id, TecnicoDTO tecnicoDTO, Long usuarioId) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(TecnicoNaoEncontradoException::new);

        if (tecnicoDTO.getNome() != null) {
            tecnico.setNome(tecnicoDTO.getNome());
        }
        if (tecnicoDTO.getCpf() != null) {
            tecnico.setCpf(tecnicoDTO.getCpf());
        }
        if (tecnicoDTO.getRegistroProfissional() != null) {
            tecnico.setRegistroProfissional(tecnicoDTO.getRegistroProfissional());
        }
        if (tecnicoDTO.getEspecialidade() != null) {
            tecnico.setEspecialidade(tecnicoDTO.getEspecialidade());
        }
        if (tecnicoDTO.getEquipamentoId() != null) {
            Equipamento equipamento = equipamentoRepository.findById(tecnicoDTO.getEquipamentoId())
                    .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado."));
            tecnico.setEquipamento(equipamento);
        }
        if (tecnicoDTO.isAtivo() != tecnico.isAtivo()) {
            tecnico.setAtivo(tecnicoDTO.isAtivo());
        }

        tecnico.setAtualizadoEm(LocalDateTime.now());
        tecnico.setAtualizadoPor(usuarioId);

        return TecnicoMapper.tecnicoToTecnicoVO(tecnicoRepository.save(tecnico));
    }


    @Transactional
    public void desativar(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(TecnicoNaoEncontradoException::new);
        tecnico.setAtivo(false);
        tecnicoRepository.save(tecnico);
    }
}
