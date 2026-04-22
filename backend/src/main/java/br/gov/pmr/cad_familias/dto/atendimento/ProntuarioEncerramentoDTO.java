// ProntuarioEncerramentoDTO.java
package br.gov.pmr.cad_familias.dto.atendimento;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProntuarioEncerramentoDTO(

        @NotNull(message = "Data de fechamento é obrigatória")
        LocalDate dataFechamento,

        String motivoEncerramento  // não obrigatório por ora
) {}
