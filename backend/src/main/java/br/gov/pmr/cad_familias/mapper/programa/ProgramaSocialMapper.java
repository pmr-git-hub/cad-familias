package br.gov.pmr.cad_familias.mapper.programa;

import br.gov.pmr.cad_familias.domain.programa.ProgramaSocial;
import br.gov.pmr.cad_familias.dto.programa.ProgramaSocialRequest;
import br.gov.pmr.cad_familias.dto.programa.ProgramaSocialResponse;
import org.springframework.stereotype.Component;

@Component
public class ProgramaSocialMapper {

    public ProgramaSocial toEntity(ProgramaSocialRequest request) {
        ProgramaSocial entity = new ProgramaSocial();
        entity.setNome(request.getNome());
        entity.setCriterios(request.getCriterios());
        entity.setOrgaoGestor(request.getOrgaoGestor());
        entity.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        return entity;
    }

    public void updateEntity(ProgramaSocial entity, ProgramaSocialRequest request) {
        entity.setNome(request.getNome());
        entity.setCriterios(request.getCriterios());
        entity.setOrgaoGestor(request.getOrgaoGestor());
        if (request.getAtivo() != null) {
            entity.setAtivo(request.getAtivo());
        }
    }

    public ProgramaSocialResponse toResponse(ProgramaSocial entity) {
        ProgramaSocialResponse r = new ProgramaSocialResponse();
        r.setId(entity.getId());
        r.setNome(entity.getNome());
        r.setCriterios(entity.getCriterios());
        r.setOrgaoGestor(entity.getOrgaoGestor());
        r.setAtivo(entity.isAtivo());
        r.setCriadoEm(entity.getCriadoEm());
        r.setCriadoPor(entity.getCriadoPor());
        r.setAtualizadoEm(entity.getAtualizadoEm());
        r.setAtualizadoPor(entity.getAtualizadoPor());
        return r;
    }
}
