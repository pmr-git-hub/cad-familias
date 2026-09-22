package br.gov.pmr.cad_familias.dto.servico;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class VinculoPessoaServicoLoteRequest {

    @NotNull(message = "ID do serviço é obrigatório")
    private Long servicoId;

    @NotEmpty(message = "Selecione pelo menos uma pessoa")
    private List<Long> pessoaIds;

    private LocalDate dataEntrada;
}
