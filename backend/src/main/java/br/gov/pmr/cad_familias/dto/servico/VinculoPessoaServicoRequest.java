// VinculoPessoaServicoRequest.java
package br.gov.pmr.cad_familias.dto.servico;

import br.gov.pmr.cad_familias.domain.programa.StatusVinculo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VinculoPessoaServicoRequest {

    @NotNull(message = "ID da pessoa é obrigatório")
    private Long pessoaId;

    @NotNull(message = "ID do serviço é obrigatório")
    private Long servicoId;

    private LocalDate dataEntrada;

    private StatusVinculo status;

}
