package br.gov.pmr.cad_familias.mapper.servico;

import br.gov.pmr.cad_familias.domain.familia.Pessoa;
import br.gov.pmr.cad_familias.domain.servico.Servico;
import br.gov.pmr.cad_familias.domain.servico.VinculoPessoaServico;
import br.gov.pmr.cad_familias.dto.servico.VinculoPessoaServicoRequest;
import br.gov.pmr.cad_familias.dto.servico.VinculoPessoaServicoResponse;
import org.springframework.stereotype.Component;

@Component
public class VinculoPessoaServicoMapper {

    public VinculoPessoaServico toEntity(VinculoPessoaServicoRequest request, Pessoa pessoa, Servico servico) {
        VinculoPessoaServico entity = new VinculoPessoaServico();
        entity.setPessoa(pessoa);
        entity.setServico(servico);
        entity.setDataEntrada(request.getDataEntrada());
        entity.setStatus(request.getStatus());
        return entity;
    }

    public VinculoPessoaServicoResponse toResponse(VinculoPessoaServico entity) {
        VinculoPessoaServicoResponse r = new VinculoPessoaServicoResponse();
        r.setId(entity.getId());
        r.setPessoaId(entity.getPessoa().getId());
        r.setPessoaNome(entity.getPessoa().getNome());
        r.setServicoId(entity.getServico().getId());
        r.setServicoNome(entity.getServico().getNome());
        r.setDataEntrada(entity.getDataEntrada());
        r.setDataSaida(entity.getDataSaida());
        r.setStatus(entity.getStatus());
        r.setMotivoSaida(entity.getMotivoSaida());
        r.setCriadoEm(entity.getCriadoEm());
        r.setCriadoPor(entity.getCriadoPor());
        r.setAtualizadoEm(entity.getAtualizadoEm());
        r.setAtualizadoPor(entity.getAtualizadoPor());
        return r;
    }
}
