package br.gov.pmr.cad_familias.service.tecnico;

import br.gov.pmr.cad_familias.VO.tecnico.TecnicoVO;
import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.excecao.TecnicoNaoEncontradoException;
import br.gov.pmr.cad_familias.mapper.tecnico.TecnicoMapper;
import br.gov.pmr.cad_familias.repository.equipamento.EquipamentoRepository;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    public List<TecnicoVO> listarTecnicos() {
        return TecnicoMapper.listaTecnicosToVO(tecnicoRepository.findAll());
    }

    public TecnicoVO buscarPorId(Long id) {
        return tecnicoRepository.findById(id)
                .map(TecnicoMapper::tecnicoToTecnicoVO)
                .orElseThrow(TecnicoNaoEncontradoException::new);
    }

    @Transactional
    public TecnicoVO salvar(TecnicoVO tecnicoVO) {
        Equipamento equipamento = equipamentoRepository.findById(tecnicoVO.getEquipamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado."));

        Tecnico tecnico = TecnicoMapper.tecnicoVOToTecnico(tecnicoVO, equipamento);
        return TecnicoMapper.tecnicoToTecnicoVO(tecnicoRepository.save(tecnico));
    }

    @Transactional
    public TecnicoVO editar(Long id, TecnicoVO tecnicoVO) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(TecnicoNaoEncontradoException::new);

        Equipamento equipamento = equipamentoRepository.findById(tecnicoVO.getEquipamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado."));

        tecnico.setNome(tecnicoVO.getNome());
        tecnico.setCpf(tecnicoVO.getCpf());
        tecnico.setRegistroProfissional(tecnicoVO.getRegistroProfissional());
        tecnico.setEspecialidade(tecnicoVO.getEspecialidade());
        tecnico.setEquipamento(equipamento);
        tecnico.setAtivo(tecnicoVO.isAtivo());

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
