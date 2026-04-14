package br.gov.pmr.cad_familias.mapper.tecnico;

import br.gov.pmr.cad_familias.dto.tecnico.TecnicoDTO;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;

import java.util.List;

public class TecnicoMapper {

    public static TecnicoDTO tecnicoToDTO(Tecnico tecnico) {
        if (tecnico == null) return null;

        TecnicoDTO dto = new TecnicoDTO();
        dto.setId(tecnico.getId());
        dto.setNome(tecnico.getNome());
        dto.setCpf(tecnico.getCpf());
        dto.setRegistroProfissional(tecnico.getRegistroProfissional());
        dto.setEspecialidade(tecnico.getEspecialidade());
        dto.setAtivo(tecnico.isAtivo());

        return dto;
    }

    public static Tecnico dtoToTecnico(TecnicoDTO dto) {
        if (dto == null) return null;

        Tecnico tecnico = new Tecnico();
        tecnico.setId(dto.getId());
        tecnico.setNome(dto.getNome());
        tecnico.setCpf(dto.getCpf());
        tecnico.setRegistroProfissional(dto.getRegistroProfissional());
        tecnico.setEspecialidade(dto.getEspecialidade());
        tecnico.setAtivo(dto.isAtivo());

        return tecnico;
    }

    public static List<TecnicoDTO> listToDTO(List<Tecnico> tecnicos) {
        return tecnicos.stream()
                .map(TecnicoMapper::tecnicoToDTO)
                .toList();
    }
}
