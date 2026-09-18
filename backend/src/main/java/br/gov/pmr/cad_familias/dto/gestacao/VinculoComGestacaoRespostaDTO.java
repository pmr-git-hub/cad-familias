package br.gov.pmr.cad_familias.dto.gestacao;

import br.gov.pmr.cad_familias.dto.servico.VinculoPessoaServicoResponse;

import java.util.List;

public record VinculoComGestacaoRespostaDTO(
        VinculoPessoaServicoResponse vinculo,
        GestacaoRespostaDTO gestacao,
        List<String> alertas
) {}
