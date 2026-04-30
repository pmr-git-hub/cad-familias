// VinculoPessoaServicoResponse.java
package br.gov.pmr.cad_familias.dto.servico;

import br.gov.pmr.cad_familias.domain.programa.StatusVinculo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class VinculoPessoaServicoResponse {

    private Long id;
    private Long pessoaId;
    private String pessoaNome;
    private Long servicoId;
    private String servicoNome;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private StatusVinculo status;
    private String motivoSaida;
    private LocalDateTime criadoEm;
    private Long criadoPor;
    private LocalDateTime atualizadoEm;
    private Long atualizadoPor;

}
