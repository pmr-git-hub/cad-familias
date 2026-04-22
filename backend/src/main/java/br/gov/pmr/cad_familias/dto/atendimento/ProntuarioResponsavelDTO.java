// ProntuarioResponsavelDTO.java
package br.gov.pmr.cad_familias.dto.atendimento;

import jakarta.validation.constraints.NotNull;

public record ProntuarioResponsavelDTO(

        @NotNull(message = "Técnico responsável é obrigatório")
        Long tecnicoId
) {}
