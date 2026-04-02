package br.gov.pmr.cad_familias.mapper.tecnico;

import br.gov.pmr.cad_familias.VO.tecnico.TecnicoVO;
import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;

import java.util.List;

public class TecnicoMapper {

    public static TecnicoVO tecnicoToTecnicoVO(Tecnico tecnico) {
        if (tecnico == null) return null;

        TecnicoVO vo = new TecnicoVO();
        vo.setId(tecnico.getId());
        vo.setNome(tecnico.getNome());
        vo.setCpf(tecnico.getCpf());
        vo.setRegistroProfissional(tecnico.getRegistroProfissional());
        vo.setEspecialidade(tecnico.getEspecialidade());
        vo.setAtivo(tecnico.isAtivo());

        if (tecnico.getEquipamento() != null) {
            vo.setEquipamentoId(tecnico.getEquipamento().getId());
            vo.setNomeEquipamento(tecnico.getEquipamento().getNome());
        }

        return vo;
    }

    public static Tecnico tecnicoVOToTecnico(TecnicoVO vo, Equipamento equipamento) {
        if (vo == null) return null;

        Tecnico tecnico = new Tecnico();
        tecnico.setId(vo.getId());
        tecnico.setNome(vo.getNome());
        tecnico.setCpf(vo.getCpf());
        tecnico.setRegistroProfissional(vo.getRegistroProfissional());
        tecnico.setEspecialidade(vo.getEspecialidade());
        tecnico.setAtivo(vo.isAtivo());
        tecnico.setEquipamento(equipamento);

        return tecnico;
    }

    public static List<TecnicoVO> listaTecnicosToVO(List<Tecnico> tecnicos) {
        return tecnicos.stream()
                .map(TecnicoMapper::tecnicoToTecnicoVO)
                .toList();
    }
}
