package br.gov.pmr.cad_familias.dto.atendimento;

import br.gov.pmr.cad_familias.domain.atendimento.StatusProntuario;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProntuarioAtualizacaoDTO(

        @NotNull(message = "Técnico responsável é obrigatório")
        Long tecnicoId,

        @NotNull(message = "Status é obrigatório")
        StatusProntuario status,

        LocalDate dataFechamento
) {}
