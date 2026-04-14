package br.gov.pmr.cad_familias.dto.encaminhamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EncaminhamentoCadastroDTO(
        @NotNull(message = "O ID da família é obrigatório.")
        Long familiaId,

        @NotNull(message = "O ID do equipamento de origem é obrigatório.")
        Long equipamentoOrigemId,

        @NotNull(message = "O ID do equipamento de destino é obrigatório.")
        Long equipamentoDestinoId,

        @NotNull(message = "O ID do técnico é obrigatório.")
        Long tecnicoId,

        @NotBlank(message = "O motivo do encaminhamento é obrigatório.")
        String motivo
) {}
