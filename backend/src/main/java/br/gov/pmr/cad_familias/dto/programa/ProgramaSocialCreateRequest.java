package br.gov.pmr.cad_familias.dto.programa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProgramaSocialCreateRequest {

    @NotBlank(message = "Nome do programa é obrigatório")
    @Size(max = 300, message = "Nome deve ter no máximo 300 caracteres")
    private String nome;

    private String criterios;

    private String orgaoGestor;

    private Boolean ativo;

    public ProgramaSocialCreateRequest() {}


}
