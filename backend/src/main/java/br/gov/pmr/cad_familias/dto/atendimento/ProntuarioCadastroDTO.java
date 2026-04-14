package br.gov.pmr.cad_familias.dto.atendimento;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProntuarioCadastroDTO(

        @NotNull(message = "Família é obrigatória")
        Long familiaId,

        @NotNull(message = "Equipamento é obrigatório")
        Long equipamentoId,

        @NotNull(message = "Técnico responsável é obrigatório")
        Long tecnicoId,

        @NotNull(message = "Data de abertura é obrigatória")
        LocalDate dataAbertura
) {}
