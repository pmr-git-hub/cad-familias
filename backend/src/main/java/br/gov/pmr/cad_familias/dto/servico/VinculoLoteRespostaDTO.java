package br.gov.pmr.cad_familias.dto.servico;

import br.gov.pmr.cad_familias.dto.gestacao.VinculoComGestacaoRespostaDTO;

import java.util.List;

public record VinculoLoteRespostaDTO(
        List<VinculoComGestacaoRespostaDTO> sucesso,
        List<VinculoLoteFalhaDTO> falhas
) {}
