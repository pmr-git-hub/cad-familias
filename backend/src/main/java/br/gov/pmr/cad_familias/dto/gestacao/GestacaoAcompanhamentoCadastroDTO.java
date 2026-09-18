package br.gov.pmr.cad_familias.dto.gestacao;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GestacaoAcompanhamentoCadastroDTO(
        @NotNull LocalDate dataRegistro,
        Integer semanasGestacao,
        Integer numeroConsultas,
        @NotNull Boolean altoRisco,
        String motivoAltoRisco,
        BigDecimal pesoKg,
        String pressaoArterial,
        String observacoes
) {}
