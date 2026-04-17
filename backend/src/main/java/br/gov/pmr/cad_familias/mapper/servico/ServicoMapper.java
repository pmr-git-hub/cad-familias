package br.gov.pmr.cad_familias.mapper.servico;

import br.gov.pmr.cad_familias.domain.servico.Servico;
import br.gov.pmr.cad_familias.dto.servico.ServicoCreateRequest;
import br.gov.pmr.cad_familias.dto.servico.ServicoResponse;
import br.gov.pmr.cad_familias.dto.servico.ServicoUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {

    public Servico toEntity(ServicoCreateRequest request) {
        Servico entity = new Servico();
        entity.setEquipamentoId(request.getEquipamentoId());
        entity.setNome(request.getNome());
        entity.setDescricao(request.getDescricao());
        entity.setPublicoAlvo(request.getPublicoAlvo());
        entity.setFaixaEtariaMin(request.getFaixaEtariaMin());
        entity.setFaixaEtariaMax(request.getFaixaEtariaMax());
        entity.setDiaSemana(request.getDiaSemana());
        entity.setHorario(request.getHorario());
        entity.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        return entity;
    }

    public void updateEntity(Servico entity, ServicoUpdateRequest request) {
        if (request.getEquipamentoId() != null) {
            entity.setEquipamentoId(request.getEquipamentoId());
        }
        if (request.getNome() != null && !request.getNome().isBlank()) {
            entity.setNome(request.getNome());
        }
        if (request.getDescricao() != null) {
            entity.setDescricao(request.getDescricao());
        }
        if (request.getPublicoAlvo() != null) {
            entity.setPublicoAlvo(request.getPublicoAlvo());
        }
        if (request.getFaixaEtariaMin() != null) {
            entity.setFaixaEtariaMin(request.getFaixaEtariaMin());
        }
        if (request.getFaixaEtariaMax() != null) {
            entity.setFaixaEtariaMax(request.getFaixaEtariaMax());
        }
        if (request.getDiaSemana() != null) {
            entity.setDiaSemana(request.getDiaSemana());
        }
        if (request.getHorario() != null) {
            entity.setHorario(request.getHorario());
        }
        if (request.getAtivo() != null) {
            entity.setAtivo(request.getAtivo());
        }
    }

    public ServicoResponse toResponse(Servico entity) {
        ServicoResponse r = new ServicoResponse();
        r.setId(entity.getId());
        r.setEquipamentoId(entity.getEquipamentoId());
        r.setNome(entity.getNome());
        r.setDescricao(entity.getDescricao());
        r.setPublicoAlvo(entity.getPublicoAlvo());
        r.setFaixaEtariaMin(entity.getFaixaEtariaMin());
        r.setFaixaEtariaMax(entity.getFaixaEtariaMax());
        r.setDiaSemana(entity.getDiaSemana());
        r.setHorario(entity.getHorario());
        r.setAtivo(entity.isAtivo());
        r.setCriadoEm(entity.getCriadoEm());
        r.setCriadoPor(entity.getCriadoPor());
        r.setAtualizadoEm(entity.getAtualizadoEm());
        r.setAtualizadoPor(entity.getAtualizadoPor());
        return r;
    }
}
