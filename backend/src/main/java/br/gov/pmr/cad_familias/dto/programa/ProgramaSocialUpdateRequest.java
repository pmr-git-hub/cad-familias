package br.gov.pmr.cad_familias.dto.programa;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramaSocialUpdateRequest {

    @Size(max = 300, message = "Nome deve ter no máximo 300 caracteres")
    private String nome;

    private String criterios;

    private String orgaoGestor;

    private Boolean ativo;
}
