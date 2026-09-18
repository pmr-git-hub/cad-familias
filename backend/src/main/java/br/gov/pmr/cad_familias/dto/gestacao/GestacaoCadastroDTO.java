package br.gov.pmr.cad_familias.dto.gestacao;

import java.time.LocalDate;

public record GestacaoCadastroDTO(
        LocalDate dataUltimaMenstruacao,
        LocalDate dataPrevistaParto
) {}
