package br.gov.pmr.cad_familias.mapper.programa;

import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.domain.programa.ProgramaSocial;
import br.gov.pmr.cad_familias.domain.programa.VinculoFamiliaPrograma;
import br.gov.pmr.cad_familias.dto.programa.VinculoFamiliaProgramaRequest;
import br.gov.pmr.cad_familias.dto.programa.VinculoFamiliaProgramaResponse;
import org.springframework.stereotype.Component;

@Component
public class VinculoFamiliaProgramaMapper {

    public VinculoFamiliaPrograma toEntity(VinculoFamiliaProgramaRequest request, Familia familia, ProgramaSocial programa) {
        VinculoFamiliaPrograma entity = new VinculoFamiliaPrograma();
        entity.setFamilia(familia);
        entity.setPrograma(programa);
        entity.setDataEntrada(request.getDataEntrada());
        entity.setStatus(request.getStatus());
        return entity;
    }

    public VinculoFamiliaProgramaResponse toResponse(VinculoFamiliaPrograma entity) {
        VinculoFamiliaProgramaResponse r = new VinculoFamiliaProgramaResponse();
        r.setId(entity.getId());
        r.setFamiliaId(entity.getFamilia().getId());
        r.setProgramaId(entity.getPrograma().getId());
        r.setProgramaNome(entity.getPrograma().getNome());
        r.setDataEntrada(entity.getDataEntrada());
        r.setDataSaida(entity.getDataSaida());
        r.setStatus(entity.getStatus());
        r.setMotivoSaida(entity.getMotivоSaida());
        r.setCriadoEm(entity.getCriadoEm());
        r.setCriadoPor(entity.getCriadoPor());
        r.setAtualizadoEm(entity.getAtualizadoEm());
        r.setAtualizadoPor(entity.getAtualizadoPor());
        return r;
    }
}
