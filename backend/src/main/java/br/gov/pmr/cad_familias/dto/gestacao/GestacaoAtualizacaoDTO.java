package br.gov.pmr.cad_familias.dto.gestacao;

import br.gov.pmr.cad_familias.domain.gestacao.StatusGestacao;

import java.time.LocalDate;

public record GestacaoAtualizacaoDTO(
        LocalDate dataUltimaMenstruacao,
        LocalDate dataPrevistaParto,
        StatusGestacao status
) {}
