package br.gov.pmr.cad_familias.dto.encaminhamento;

import br.gov.pmr.cad_familias.domain.encaminhamento.StatusEncaminhamento;
import jakarta.validation.constraints.NotNull;

public record EncaminhamentoAtualizacaoStatusDTO(
        @NotNull(message = "O novo status é obrigatório.")
        StatusEncaminhamento status
) {}
