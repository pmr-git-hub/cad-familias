package br.gov.pmr.cad_familias.dto.atendimento;

import jakarta.validation.constraints.NotNull;

public record ProntuarioCadastroDTO(

        @NotNull(message = "Família é obrigatória")
        Long familiaId,

        @NotNull(message = "Equipamento é obrigatório")
        Long equipamentoId
) {}
