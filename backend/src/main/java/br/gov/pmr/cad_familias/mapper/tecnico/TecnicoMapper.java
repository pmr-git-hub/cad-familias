package br.gov.pmr.cad_familias.mapper.tecnico;

import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.domain.tecnico.TecnicoEquipamento;
import br.gov.pmr.cad_familias.dto.tecnico.TecnicoDTO;
import br.gov.pmr.cad_familias.dto.tecnico.TecnicoEquipamentoDTO;

import java.util.Collections;
import java.util.List;

public class TecnicoMapper {

    private TecnicoMapper() {}

    public static TecnicoDTO tecnicoToDTO(Tecnico tecnico, List<TecnicoEquipamento> vinculos) {
        TecnicoDTO dto = new TecnicoDTO();
        dto.setId(tecnico.getId());
        dto.setNome(tecnico.getNome());
        dto.setCpf(tecnico.getCpf());
        dto.setRegistroProfissional(tecnico.getRegistroProfissional());
        dto.setEspecialidade(tecnico.getEspecialidade());
        dto.setAtivo(tecnico.isAtivo());
        dto.setEquipamentos(vinculosToDTO(vinculos));
        return dto;
    }

    public static TecnicoDTO tecnicoToDTO(Tecnico tecnico) {
        return tecnicoToDTO(tecnico, Collections.emptyList());
    }

    public static Tecnico dtoToTecnico(TecnicoDTO dto) {
        Tecnico tecnico = new Tecnico();
        tecnico.setNome(dto.getNome());
        tecnico.setCpf(dto.getCpf());
        tecnico.setRegistroProfissional(dto.getRegistroProfissional());
        tecnico.setEspecialidade(dto.getEspecialidade());
        tecnico.setAtivo(dto.isAtivo());
        return tecnico;
    }

    public static TecnicoEquipamentoDTO vinculoToDTO(TecnicoEquipamento vinculo) {
        TecnicoEquipamentoDTO dto = new TecnicoEquipamentoDTO();
        dto.setId(vinculo.getId());
        dto.setEquipamentoId(vinculo.getEquipamento().getId());
        dto.setNomeEquipamento(vinculo.getEquipamento().getNome());
        dto.setDataInicio(vinculo.getDataInicio());
        dto.setDataFim(vinculo.getDataFim());
        dto.setAtivo(vinculo.isAtivo());
        return dto;
    }

    public static List<TecnicoEquipamentoDTO> vinculosToDTO(List<TecnicoEquipamento> vinculos) {
        if (vinculos == null) return Collections.emptyList();
        return vinculos.stream().map(TecnicoMapper::vinculoToDTO).toList();
    }

    public static List<TecnicoDTO> listToDTO(List<Tecnico> tecnicos) {
        return tecnicos.stream().map(t -> tecnicoToDTO(t)).toList();
    }
}
